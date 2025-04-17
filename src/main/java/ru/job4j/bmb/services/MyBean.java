package ru.job4j.bmb.services;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;

public class MyBean implements BeanNameAware {

    private String beanName;
    private ApplicationContext context;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Имя бина - " + name);
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
        System.out.println("ApplicationContext set in MyBean");
    }

    public void displayAllBeanNames() {
        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("Beans in ApplicationContext: ");
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}
