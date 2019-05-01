package org.springframework.fu.kofu.webflux

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafInitializer
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
import org.springframework.context.support.GenericApplicationContext
import org.springframework.fu.kofu.AbstractDsl

/**
 * Kofu DSL for Thymeleaf template engine.
 *
 * Requires a dependency on `org.springframework.boot:spring-boot-starter-thymeleaf`.
 */
class ThymeleafDsl(private val init: ThymeleafDsl.() -> Unit) : AbstractDsl() {

    private val properties = ThymeleafProperties()

    /**
     * Prefix that gets prepended to view names when building a URL.
     */
    var prefix
        get() = properties.prefix
        set(value) {
            properties.prefix = value
        }

    /**
     * Suffix that gets appended to view names when building a URL.
     */
    var suffix
        get() = properties.suffix
        set(value) {
            properties.suffix = value
        }

    /**
     * Template mode to be applied to templates. See also Thymeleaf's TemplateMode enum.
     */
    var mode
        get() = properties.mode
        set(value) {
            properties.mode = value
        }

    /**
     * Template files encoding.
     */
    var encoding
        get() = properties.encoding
        set(value) {
            properties.encoding = value
        }

    /**
     * Whether to enable template caching.
     */
    var cache
        get() = properties.isCache
        set(value) {
            properties.isCache = value
        }

    /**
     * Enable the SpringEL compiler in SpringEL expressions.
     */
    var enableSpringElCompiler
        get() = properties.isEnableSpringElCompiler
        set(value) {
            properties.isEnableSpringElCompiler = value
        }

    /**
     * Maximum size of data buffers used for writing to the response. Templates will
     * execute in CHUNKED mode by default if this is set.
     */
    var maxChunkSize
        get() = properties.reactive.maxChunkSize
        set(value) {
            properties.reactive.maxChunkSize = value
        }

    /**
     * Media types supported by the view technology.
     */
    var mediaTypes
        get() = properties.reactive.mediaTypes
        set(value) {
            properties.reactive.mediaTypes = value
        }

    override fun initialize(context: GenericApplicationContext) {
        super.initialize(context)
        init()
        ThymeleafInitializer(properties).initialize(context)
    }
}

/**
 * Configures [Thymeleaf](https://www.thymeleaf.org/) view resolver and template engine.
 *
 * Requires a dependency on `org.springframework.boot:spring-boot-starter-thymeleaf`.
 */
fun WebFluxServerDsl.thymeleaf(dsl: ThymeleafDsl.() -> Unit = {}) {
    /** Required due to module privacy **/
    val baseDsl = this;
    val context = AbstractDsl::class.java.getDeclaredField("context")
            .apply { isAccessible = true }
            .let { it.get(baseDsl) as GenericApplicationContext }
    /** End hack **/
    ThymeleafDsl(dsl).initialize(context)
}