package space.x9x.radp.test


import spock.lang.Specification

/**
 * @author IO x9x
 * @since 2024-10-08 13:08
 */
class DemoSpec extends Specification {

    def "demo test"() {
        expect:
        y == x * x

        where:
        y  || x
        25 || 5
        9  || 3
    }
}
