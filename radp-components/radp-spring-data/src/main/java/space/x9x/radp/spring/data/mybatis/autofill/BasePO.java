package space.x9x.radp.spring.data.mybatis.autofill;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 继承该类的数据库持久化对象
 * <pre>{@code
 * @TableName("demo")
 * public class DemoPO extends BasePO<DemoPO> {
 *     ... ...
 * }
 * }</pre>
 *
 * @author x9x
 * @since 2024-10-01 10:02
 */
public abstract class BasePO<T extends Model<?>> extends AutofillModel<T> {
}
