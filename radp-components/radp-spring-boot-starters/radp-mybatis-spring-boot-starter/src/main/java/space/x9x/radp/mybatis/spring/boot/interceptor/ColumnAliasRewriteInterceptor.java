/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.mybatis.spring.boot.interceptor;

import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import space.x9x.radp.mybatis.spring.boot.env.MybatisPlusExtensionProperties;
import space.x9x.radp.spring.data.mybatis.autofill.BasePO;

/**
 * MyBatis interceptor that rewrites SQL to support configurable physical column names for
 * logical properties {@code createdAt} and {@code updatedAt} while keeping the Java
 * property names stable in BasePO.
 *
 * <p>
 * Behavior: - For SELECT statements, occurrences of {@code created_at} and
 * {@code updated_at} are replaced with {@code <configured> AS created_at} and
 * {@code <configured> AS updated_at} respectively, so MyBatis can still map back to the
 * Java properties via aliases. - For non-SELECT statements (INSERT/UPDATE/DELETE),
 * occurrences are replaced with the configured column names directly.
 *
 * <p>
 * The interceptor is a lightweight string rewriter with conservative word-boundary regex
 * to avoid accidental replacements within longer identifiers. If configured names are
 * identical to defaults, it becomes a no-op.
 *
 * <p>
 * Note: This is a pragmatic solution to allow global configuration without forcing
 * per-entity annotations. If you have custom SQL with explicit aliases, ensure those are
 * compatible with this interceptor.
 *
 * @author Junie
 * @since 2025-11-10 15:04
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class ColumnAliasRewriteInterceptor implements Interceptor {

	// Scope helper: only apply rewrite for statements clearly involving BasePO
	private static boolean isBasePOScope(MappedStatement ms, Object paramObj) {
		return hasBasePOResultType(ms) || ParameterWalker.containsBasePO(paramObj);
	}

	/** Checks if any result map type is assignable to BasePO. */
	private static boolean hasBasePOResultType(MappedStatement ms) {
		try {
			if (ms != null && ms.getResultMaps() != null) {
				for (ResultMap rm : ms.getResultMaps()) {
					if (rm != null && rm.getType() != null && BasePO.class.isAssignableFrom(rm.getType())) {
						return true;
					}
				}
			}
		}
		catch (Throwable ignore) {
			// ignore and fallthrough
		}
		return false;
	}


	private static final String DEFAULT_CREATED = BasePO.LOGICAL_COL_CREATED_AT;

	private static final String DEFAULT_UPDATED = BasePO.LOGICAL_COL_UPDATED_AT;

	private static final String DEFAULT_CREATOR = BasePO.LOGICAL_COL_CREATOR;

	private static final String DEFAULT_UPDATER = BasePO.LOGICAL_COL_UPDATER;

	private final String createdPhysical;

	private final String updatedPhysical;

	private final String creatorPhysical;

	private final String updaterPhysical;

	private final java.util.List<Token> tokens;

	/** Whether to apply rewrite globally (true) or only in BasePO scope (false). */
	private final boolean globalScope;

	public ColumnAliasRewriteInterceptor(MybatisPlusExtensionProperties.SqlRewrite config) {
		this.tokens = new java.util.ArrayList<>(4);
		String createdPhysical = config == null ? null : config.getCreatedColumnName();
		String updatedPhysical = config == null ? null : config.getLastModifiedColumnName();
		String creatorPhysical = config == null ? null : config.getCreatorColumnName();
		String updaterPhysical = config == null ? null : config.getUpdaterColumnName();
		this.createdPhysical = (createdPhysical == null || createdPhysical.trim().isEmpty()) ? DEFAULT_CREATED
				: createdPhysical.trim();
		this.updatedPhysical = (updatedPhysical == null || updatedPhysical.trim().isEmpty()) ? DEFAULT_UPDATED
				: updatedPhysical.trim();
		this.creatorPhysical = (creatorPhysical == null || creatorPhysical.trim().isEmpty()) ? DEFAULT_CREATOR
				: creatorPhysical.trim();
		this.updaterPhysical = (updaterPhysical == null || updaterPhysical.trim().isEmpty()) ? DEFAULT_UPDATER
				: updaterPhysical.trim();
		// prepare tokens list from logical and physical names
		this.tokens.add(new Token(DEFAULT_CREATED, this.createdPhysical));
		this.tokens.add(new Token(DEFAULT_UPDATED, this.updatedPhysical));
		this.tokens.add(new Token(DEFAULT_CREATOR, this.creatorPhysical));
		this.tokens.add(new Token(DEFAULT_UPDATER, this.updaterPhysical));
		// scope configuration
		MybatisPlusExtensionProperties.SqlRewrite.Scope scope = config == null
				? MybatisPlusExtensionProperties.SqlRewrite.Scope.BASEPO : config.getScope();
		this.globalScope = (scope == MybatisPlusExtensionProperties.SqlRewrite.Scope.GLOBAL);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// Unwrap to the real StatementHandler to avoid proxy property issues
		StatementHandler sh = PluginUtils.realTarget((StatementHandler) invocation.getTarget());
		MetaObject mo = SystemMetaObject.forObject(sh);
		// Scope control: only apply for statements related to BasePO when not in GLOBAL
		// scope
		MappedStatement ms = (MappedStatement) mo.getValue("delegate.mappedStatement");
		BoundSql boundSql = sh.getBoundSql();
		Object paramObj = (boundSql == null ? null : boundSql.getParameterObject());
		if (!this.globalScope && !isBasePOScope(ms, paramObj)) {
			return invocation.proceed();
		}

		PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
		String sql = mpBs.sql();

		// No-op when all logical names equal configured physical names
		if (allNoop()) {
			return invocation.proceed();
		}

		// Quick short-circuit: if SQL doesn't contain any logical tokens, skip rewrite
		if (!hasAnyToken(sql)) {
			return invocation.proceed();
		}

		String trimmed = ltrim(sql);
		boolean isSelect = startsWithIgnoreCase(trimmed, "select");

		String rewritten;
		if (isSelect) {
			rewritten = rewriteSelect(sql);
		}
		else {
			rewritten = rewriteNonSelect(sql);
		}

		mpBs.sql(rewritten);
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(java.util.Properties properties) {
		// no-op
	}

	private boolean allNoop() {
		for (Token t : this.tokens) {
			if (!t.isNoop()) {
				return false;
			}
		}
		return true;
	}

	private boolean hasAnyToken(String sql) {
		for (Token t : this.tokens) {
			if (t.contains(sql)) {
				return true;
			}
		}
		return false;
	}

	private static final Pattern FROM_PATTERN = Pattern.compile("(?i)\\bfrom\\b");


	private String rewriteSelect(String sql) {
		Matcher fromMatcher = FROM_PATTERN.matcher(sql);
		if (fromMatcher.find()) {
			int idx = fromMatcher.start();
			String head = sql.substring(0, idx);
			String tail = sql.substring(idx);
			// In the SELECT list (head), alias physical columns back to logical defaults
			String newHead = head;
			for (Token t : this.tokens) {
				newHead = t.aliasHead(newHead);
			}
			// In the rest of the SQL (tail), use the physical columns without alias
			String newTail = tail;
			for (Token t : this.tokens) {
				newTail = t.replaceAll(newTail);
			}
			return newHead + newTail;
		}
		// Fallback: no FROM found, alias across entire SQL conservatively
		String out = sql;
		for (Token t : this.tokens) {
			out = t.aliasHead(out);
		}
		return out;
	}

	private String rewriteNonSelect(String sql) {
		String out = sql;
		for (Token t : this.tokens) {
			out = t.replaceAll(out);
		}
		return out;
	}

	private static Pattern buildTokenPattern(String logicalName) {
		// case-insensitive word boundary match of the logical column token
		String regex = "(?i)(?<![\\w])" + Pattern.quote(logicalName) + "(?![\\w])";
		return Pattern.compile(regex);
	}

	private static String ltrim(String s) {
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.substring(i);
	}

	private static boolean startsWithIgnoreCase(String s, String prefix) {
		int len = prefix.length();
		if (s.length() < len) {
			return false;
		}
		return s.regionMatches(true, 0, prefix, 0, len);
	}

	/**
	 * Lightweight, iterative parameter walker to detect presence of BasePO in common
	 * MyBatis parameter containers. Avoids deep recursion and guards against cycles.
	 */
	private static final class ParameterWalker {

		/**
		 * Maximum number of elements to scan in an Iterable/array to keep checks cheap.
		 */
		private static final int MAX_SCAN = 64;

		/** Common MyBatis-Plus ParamMap keys that may hold the entity. */
		private static final String[] PARAM_KEYS = { "et", "entity", "param1", "arg0", "record" };

		private ParameterWalker() {
		}

		/**
		 * Lightweight, one-level check for presence of BasePO in typical MyBatis
		 * parameter shapes. It only inspects:
		 * <ul>
		 * <li>root object itself</li>
		 * <li>known ParamMap keys: et/entity/param1/arg0/record</li>
		 * <li>immediate elements of Iterable/array (capped by MAX_SCAN)</li>
		 * </ul>
		 * This avoids recursion and expensive traversal while covering common cases.
		 * @param root parameter object from BoundSql
		 * @return true if a BasePO instance is clearly present
		 */
		static boolean containsBasePO(Object root) {
			if (root instanceof BasePO) {
				return true;
			}
			if (root == null) {
				return false;
			}
			return isBasePOOrContainerHas(root);
		}

		private static boolean isBasePOOrContainerHas(Object v) {
			if (v instanceof BasePO) {
				return true;
			}
			if (v instanceof java.util.Map) {
				java.util.Map<?, ?> map = (java.util.Map<?, ?>) v;
				for (String k : PARAM_KEYS) {
					Object mv = map.get(k);
					if (mv instanceof BasePO) {
						return true;
					}
					if (mv instanceof Iterable && hasBasePOInIterable((Iterable<?>) mv)) {
						return true;
					}
					if (mv != null && mv.getClass().isArray() && hasBasePOInArray(mv)) {
						return true;
					}
				}
				return false;
			}
			if (v instanceof Iterable) {
				return hasBasePOInIterable((Iterable<?>) v);
			}
			if (v != null && v.getClass().isArray()) {
				return hasBasePOInArray(v);
			}
			return false;
		}

		private static boolean hasBasePOInIterable(Iterable<?> it) {
			int i = 0;
			for (Object v : it) {
				if (v instanceof BasePO) {
					return true;
				}
				if (++i >= MAX_SCAN) {
					break;
				}
			}
			return false;
		}

		private static boolean hasBasePOInArray(Object arr) {
			int len = java.lang.reflect.Array.getLength(arr);
			int cap = Math.min(len, MAX_SCAN);
			for (int i = 0; i < cap; i++) {
				Object v = java.lang.reflect.Array.get(arr, i);
				if (v instanceof BasePO) {
					return true;
				}
			}
			return false;
		}

	}

	private static final class Token {

		private final String logical;

		private final String physical;

		private final Pattern pattern;

		private Token(String logical, String physical) {
			this.logical = logical;
			this.physical = physical;
			this.pattern = buildTokenPattern(logical);
		}

		private boolean isNoop() {
			return this.logical.equalsIgnoreCase(this.physical);
		}

		private boolean contains(String sql) {
			return this.pattern.matcher(sql).find();
		}

		private String aliasHead(String headSql) {
			if (isNoop()) {
				return headSql;
			}
			return this.pattern.matcher(headSql).replaceAll(this.physical + " AS " + this.logical);
		}

		private String replaceAll(String sql) {
			if (isNoop()) {
				return sql;
			}
			return this.pattern.matcher(sql).replaceAll(this.physical);
		}

	}
}
