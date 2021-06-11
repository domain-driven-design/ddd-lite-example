package com.example.config;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class ResetDbListener extends AbstractTestExecutionListener {

    @Override
    public int getOrder() {
        return 4500;
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        ResetDbService resetDbService =
                testContext.getApplicationContext().getBean(ResetDbService.class);
        resetDbService.backUp();
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ResetDbService resetDbService =
                testContext.getApplicationContext().getBean(ResetDbService.class);
        resetDbService.rollback();
    }
}
