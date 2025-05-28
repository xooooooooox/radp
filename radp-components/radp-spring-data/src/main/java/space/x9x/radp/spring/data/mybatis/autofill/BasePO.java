package space.x9x.radp.spring.data.mybatis.autofill;

import java.io.Serial;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 继承该类的数据库持久化对象 <pre>{@code
 * &#64;TableName("demo")
 * public class DemoPO extends BasePO<DemoPO> {
 *     ... ...
 * }
 * }</pre>
 *
 * @author IO x9x
 * @since 2024-10-01 10:02
 */
public abstract class BasePO<T extends Model<?>> extends AutofillModel<T> {

    @Serial
    private static final long serialVersionUID = 1L;
}
