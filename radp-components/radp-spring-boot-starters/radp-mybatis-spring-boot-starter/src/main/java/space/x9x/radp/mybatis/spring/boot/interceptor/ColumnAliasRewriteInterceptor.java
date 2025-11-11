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
import java.util.ArrayList;
import java.util.List;
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
import space.x9x.radp.spring.data.mybatis.support.MybatisEntityResolver;

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
 * @author x9x
 * @since 2025-11-10 15:04
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class ColumnAliasRewriteInterceptor implements Interceptor {

	// Scope helper: only apply rewrite for statements clearly involving BasePO
	private static boolean isBasePOScope(MappedStatement ms, Object paramObj) {
		return hasBasePOResultType(ms) || MybatisEntityResolver.containsType(paramObj, BasePO.class);
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

	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([#$]\\{[^}]+})");

	private static final String PLACEHOLDER_TOKEN_PREFIX = "__radp_param_";

	private static final String PLACEHOLDER_TOKEN_SUFFIX = "__";

	private String rewriteSelect(String sql) {
		MaskedSql masked = maskPlaceholders(sql);
		String working = masked.sql();
		Matcher fromMatcher = FROM_PATTERN.matcher(working);
		String rewritten;
		if (fromMatcher.find()) {
			int idx = fromMatcher.start();
			String head = working.substring(0, idx);
			String tail = working.substring(idx);
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
			rewritten = newHead + newTail;
		}
		else {
			// Fallback: no FROM found, alias across entire SQL conservatively
			String out = working;
			for (Token t : this.tokens) {
				out = t.aliasHead(out);
			}
			rewritten = out;
		}
		return masked.restore(rewritten);
	}

	private String rewriteNonSelect(String sql) {
		MaskedSql masked = maskPlaceholders(sql);
		String out = masked.sql();
		for (Token t : this.tokens) {
			out = t.replaceAll(out);
		}
		return masked.restore(out);
	}

	private static Pattern buildTokenPattern(String name) {
		// case-insensitive word boundary match of the column token
		String regex = "(?i)(?<![\\w])" + Pattern.quote(name) + "(?![\\w])";
		return Pattern.compile(regex);
	}

	private static Pattern buildPhysicalHeadPattern(String physical, String logical) {
		// ensure we don't alias twice when SQL already contains "physical AS logical"
		String aliasSuffix = "\\s+AS\\s+" + Pattern.quote(logical);
		String regex = "(?i)(?<![\\w])" + Pattern.quote(physical) + "(?![\\w])(?!(?:"
				+ aliasSuffix + "))";
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

	private MaskedSql maskPlaceholders(String sql) {
		java.util.regex.Matcher matcher = PLACEHOLDER_PATTERN.matcher(sql);
		StringBuffer buffer = new StringBuffer();
		List<String> originals = new ArrayList<>();
		while (matcher.find()) {
			String token = placeholderToken(originals.size());
			originals.add(matcher.group(1));
			matcher.appendReplacement(buffer, Matcher.quoteReplacement(token));
		}
		matcher.appendTail(buffer);
		return new MaskedSql(buffer.toString(), originals);
	}

	private static String placeholderToken(int index) {
		return PLACEHOLDER_TOKEN_PREFIX + index + PLACEHOLDER_TOKEN_SUFFIX;
	}

	private static final class Token {

		private final String logical;

		private final String physical;

		private final Pattern logicalPattern;

		private final Pattern physicalPattern;

		private final Pattern physicalHeadPattern;

		private Token(String logical, String physical) {
			this.logical = logical;
			this.physical = physical;
			this.logicalPattern = buildTokenPattern(logical);
			this.physicalPattern = buildTokenPattern(physical);
			this.physicalHeadPattern = buildPhysicalHeadPattern(physical, logical);
		}

		private boolean isNoop() {
			return this.logical.equalsIgnoreCase(this.physical);
		}

		private boolean contains(String sql) {
			if (this.logicalPattern.matcher(sql).find()) {
				return true;
			}
			return !isNoop() && this.physicalPattern.matcher(sql).find();
		}

		private String aliasHead(String headSql) {
			if (isNoop()) {
				return headSql;
			}
			String out = this.logicalPattern.matcher(headSql).replaceAll(this.physical + " AS " + this.logical);
			return this.physicalHeadPattern.matcher(out).replaceAll(this.physical + " AS " + this.logical);
		}

		private String replaceAll(String sql) {
			if (isNoop()) {
				return sql;
			}
			return this.logicalPattern.matcher(sql).replaceAll(this.physical);
		}

	}

	private static final class MaskedSql {

		private final String sql;

		private final List<String> placeholders;

		MaskedSql(String sql, List<String> placeholders) {
			this.sql = sql;
			this.placeholders = placeholders;
		}

		String sql() {
			return this.sql;
		}

		String restore(String rewritten) {
			String result = rewritten;
			for (int i = 0; i < this.placeholders.size(); i++) {
				result = result.replace(placeholderToken(i), this.placeholders.get(i));
			}
			return result;
		}

	}

}
