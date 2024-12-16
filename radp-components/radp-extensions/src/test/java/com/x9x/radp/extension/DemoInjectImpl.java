package com.x9x.radp.extension;

/**
 * @author x9x
 * @since 2024-09-25 14:51
 */
public class DemoInjectImpl implements DemoInject {
    @Override
    public void echo() {
        System.out.println("echo from demoInjectImpl");
    }
}
