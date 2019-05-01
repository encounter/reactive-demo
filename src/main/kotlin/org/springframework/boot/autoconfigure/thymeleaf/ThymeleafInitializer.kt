package org.springframework.boot.autoconfigure.thymeleaf

import org.springframework.beans.factory.getBean
import org.springframework.beans.factory.getBeanProvider
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.*
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean

/**
 * [ApplicationContextInitializer] adapter for [ThymeleafAutoConfiguration].
 */
class ThymeleafInitializer(private val properties: ThymeleafProperties) : ApplicationContextInitializer<GenericApplicationContext> {

    override fun initialize(context: GenericApplicationContext) {
        val templateResolverConfiguration = DefaultTemplateResolverConfiguration(this.properties, context)
        context.registerBean {
            templateResolverConfiguration.checkTemplateLocationExists()
            templateResolverConfiguration.defaultTemplateResolver()
        }

        val reactiveConfiguration = ThymeleafReactiveConfiguration()
        context.registerBean { reactiveConfiguration.templateEngine(properties, context.getBeanProvider(), context.getBeanProvider()) }

        val webFluxConfiguration = ThymeleafWebFluxConfiguration()
        context.registerBean { webFluxConfiguration.thymeleafViewResolver(context.getBean(), properties) }

        val java8TimeDialectConfiguration = ThymeleafJava8TimeDialect()
        context.registerBean { java8TimeDialectConfiguration.java8TimeDialect() }
    }
}
