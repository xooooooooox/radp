package space.x9x.radp.spring.framework.json


import space.x9x.radp.spring.framework.json.fastjson.Fastjson
import space.x9x.radp.spring.framework.json.fastjson2.Fastjson2
import space.x9x.radp.spring.framework.json.gson.Gson
import space.x9x.radp.spring.framework.json.jackson.Jackson
import space.x9x.radp.spring.framework.json.support.JSONHelper
import spock.lang.Specification

/**
 * @author IO x9x
 * @since 2024-09-26 11:49
 */
class JSONSpec extends Specification {

    TestCase testCase = TestCase.builder()
            .id(1L)
            .username("bob")
            .active(true)
            .build()

    static testJson = "{\"id\":1,\"username\":\"bob\",\"active\":true}"

    def "test extensions"() {
        expect:
        extension == JSONHelper.json(spi).getClass()

        where:
        spi         || extension
        "jackson"   || Jackson.class
        "fastjson"  || Fastjson.class
        "fastjson2" || Fastjson2.class
        "gson"      || Gson.class
    }

    def "test toJSONString"() {
        expect:
        expectedJson == JSONHelper.json(spi).toJSONString(testCase)

        where:
        spi         || expectedJson
        "jackson"   || testJson
        "fastjson"  || testJson
        "fastjson2" || testJson
        "gson"      || testJson
    }

    def "test parseObject"() {
        expect:
        testCase == JSONHelper.json(spi).parseObject(json, TestCase.class)

        where:
        spi         || json
        "jackson"   || testJson
        "fastjson"  || testJson
        "fastjson2" || testJson
        "gson"      || testJson
    }

}
