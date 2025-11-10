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
	private static boolean isBasePOScope(MappedStatement ms, MetaObject mo) {
		try {
			if (ms != null && ms.getResultMaps() != null) {
				for (ResultMap rm : ms.getResultMaps()) {
					if (rm != null && rm.getType() != null && space.x9x.radp.spring.data.mybatis.autofill.BasePO.class
						.isAssignableFrom(rm.getType())) {
						return true;
					}
				}
			}
		}
		catch (Throwable ignore) {
			// ignore
		}
		try {
			Object paramObj = mo.getValue("delegate.boundSql.parameterObject");
			return isOrContainsBasePO(paramObj);
		}
		catch (Throwable ignore) {
			return false;
		}
	}

	private static boolean isOrContainsBasePO(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof space.x9x.radp.spring.data.mybatis.autofill.BasePO) {
			return true;
		}
		if (obj instanceof java.util.Map) {
			for (Object v : ((java.util.Map<?, ?>) obj).values()) {
				if (isOrContainsBasePO(v)) {
					return true;
				}
			}
		}
		if (obj instanceof Iterable) {
			for (Object v : (Iterable<?>) obj) {
				if (isOrContainsBasePO(v)) {
					return true;
				}
			}
		}
		if (obj.getClass().isArray()) {
			int len = java.lang.reflect.Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				Object v = java.lang.reflect.Array.get(obj, i);
				if (isOrContainsBasePO(v)) {
					return true;
				}
			}
		}
		return false;
	}

	private static final String DEFAULT_CREATED = "created_at";

	private static final String DEFAULT_UPDATED = "updated_at";

	private static final String DEFAULT_CREATOR = "creator";

	private static final String DEFAULT_UPDATER = "updater";

	private final String createdPhysical;

	private final String updatedPhysical;

	private final String creatorPhysical;

	private final String updaterPhysical;

	private final Pattern createdTokenPattern = Pattern.compile("(?i)(?<![\\w])created_at(?![\\w])");

	private final Pattern updatedTokenPattern = Pattern.compile("(?i)(?<![\\w])updated_at(?![\\w])");

	private final Pattern creatorTokenPattern = Pattern.compile("(?i)(?<![\\w])creator(?![\\w])");

	private final Pattern updaterTokenPattern = Pattern.compile("(?i)(?<![\\w])updater(?![\\w])");

	public ColumnAliasRewriteInterceptor(String createdPhysical, String updatedPhysical, String creatorPhysical,
			String updaterPhysical) {
		this.createdPhysical = (createdPhysical == null || createdPhysical.trim().isEmpty()) ? DEFAULT_CREATED
				: createdPhysical.trim();
		this.updatedPhysical = (updatedPhysical == null || updatedPhysical.trim().isEmpty()) ? DEFAULT_UPDATED
				: updatedPhysical.trim();
		this.creatorPhysical = (creatorPhysical == null || creatorPhysical.trim().isEmpty()) ? DEFAULT_CREATOR
				: creatorPhysical.trim();
		this.updaterPhysical = (updaterPhysical == null || updaterPhysical.trim().isEmpty()) ? DEFAULT_UPDATER
				: updaterPhysical.trim();
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// Unwrap to the real StatementHandler to avoid proxy property issues
		StatementHandler sh = PluginUtils.realTarget((StatementHandler) invocation.getTarget());
		MetaObject mo = SystemMetaObject.forObject(sh);
		// Scope control: only apply for statements related to BasePO
		MappedStatement ms = (MappedStatement) mo.getValue("delegate.mappedStatement");
		if (!isBasePOScope(ms, mo)) {
			return invocation.proceed();
		}

		BoundSql boundSql = sh.getBoundSql();
		PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
		String sql = mpBs.sql();

		// No-op when configured names equal defaults
		boolean sameCreated = DEFAULT_CREATED.equalsIgnoreCase(this.createdPhysical);
		boolean sameUpdated = DEFAULT_UPDATED.equalsIgnoreCase(this.updatedPhysical);
		boolean sameCreator = DEFAULT_CREATOR.equalsIgnoreCase(this.creatorPhysical);
		boolean sameUpdater = DEFAULT_UPDATER.equalsIgnoreCase(this.updaterPhysical);
		if (sameCreated && sameUpdated && sameCreator && sameUpdater) {
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

	private static final Pattern FROM_PATTERN = Pattern.compile("(?i)\\bfrom\\b");

	private String rewriteSelect(String sql) {
		Matcher fromMatcher = FROM_PATTERN.matcher(sql);
		if (fromMatcher.find()) {
			int idx = fromMatcher.start();
			String head = sql.substring(0, idx);
			String tail = sql.substring(idx);
			// In the SELECT list (head), alias physical columns back to logical defaults
			String newHead = head;
			if (!DEFAULT_CREATED.equalsIgnoreCase(this.createdPhysical)) {
				newHead = this.createdTokenPattern.matcher(newHead)
					.replaceAll(this.createdPhysical + " AS " + DEFAULT_CREATED);
			}
			if (!DEFAULT_UPDATED.equalsIgnoreCase(this.updatedPhysical)) {
				newHead = this.updatedTokenPattern.matcher(newHead)
					.replaceAll(this.updatedPhysical + " AS " + DEFAULT_UPDATED);
			}
			if (!DEFAULT_CREATOR.equalsIgnoreCase(this.creatorPhysical)) {
				newHead = this.creatorTokenPattern.matcher(newHead)
					.replaceAll(this.creatorPhysical + " AS " + DEFAULT_CREATOR);
			}
			if (!DEFAULT_UPDATER.equalsIgnoreCase(this.updaterPhysical)) {
				newHead = this.updaterTokenPattern.matcher(newHead)
					.replaceAll(this.updaterPhysical + " AS " + DEFAULT_UPDATER);
			}
			// In the rest of the SQL (tail), use the physical columns without alias
			String newTail = tail;
			if (!DEFAULT_CREATED.equalsIgnoreCase(this.createdPhysical)) {
				newTail = this.createdTokenPattern.matcher(newTail).replaceAll(this.createdPhysical);
			}
			if (!DEFAULT_UPDATED.equalsIgnoreCase(this.updatedPhysical)) {
				newTail = this.updatedTokenPattern.matcher(newTail).replaceAll(this.updatedPhysical);
			}
			if (!DEFAULT_CREATOR.equalsIgnoreCase(this.creatorPhysical)) {
				newTail = this.creatorTokenPattern.matcher(newTail).replaceAll(this.creatorPhysical);
			}
			if (!DEFAULT_UPDATER.equalsIgnoreCase(this.updaterPhysical)) {
				newTail = this.updaterTokenPattern.matcher(newTail).replaceAll(this.updaterPhysical);
			}
			return newHead + newTail;
		}
		// Fallback: no FROM found, alias across entire SQL conservatively
		String out = sql;
		if (!DEFAULT_CREATED.equalsIgnoreCase(this.createdPhysical)) {
			out = this.createdTokenPattern.matcher(out).replaceAll(this.createdPhysical + " AS " + DEFAULT_CREATED);
		}
		if (!DEFAULT_UPDATED.equalsIgnoreCase(this.updatedPhysical)) {
			out = this.updatedTokenPattern.matcher(out).replaceAll(this.updatedPhysical + " AS " + DEFAULT_UPDATED);
		}
		if (!DEFAULT_CREATOR.equalsIgnoreCase(this.creatorPhysical)) {
			out = this.creatorTokenPattern.matcher(out).replaceAll(this.creatorPhysical + " AS " + DEFAULT_CREATOR);
		}
		if (!DEFAULT_UPDATER.equalsIgnoreCase(this.updaterPhysical)) {
			out = this.updaterTokenPattern.matcher(out).replaceAll(this.updaterPhysical + " AS " + DEFAULT_UPDATER);
		}
		return out;
	}

	private String rewriteNonSelect(String sql) {
		String out = sql;
		if (!DEFAULT_CREATED.equalsIgnoreCase(this.createdPhysical)) {
			out = this.createdTokenPattern.matcher(out).replaceAll(this.createdPhysical);
		}
		if (!DEFAULT_UPDATED.equalsIgnoreCase(this.updatedPhysical)) {
			out = this.updatedTokenPattern.matcher(out).replaceAll(this.updatedPhysical);
		}
		if (!DEFAULT_CREATOR.equalsIgnoreCase(this.creatorPhysical)) {
			out = this.creatorTokenPattern.matcher(out).replaceAll(this.creatorPhysical);
		}
		if (!DEFAULT_UPDATER.equalsIgnoreCase(this.updaterPhysical)) {
			out = this.updaterTokenPattern.matcher(out).replaceAll(this.updaterPhysical);
		}
		return out;
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

}
