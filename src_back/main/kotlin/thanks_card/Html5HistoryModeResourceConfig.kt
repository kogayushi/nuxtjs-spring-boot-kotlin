package thanks_card

import lombok.RequiredArgsConstructor
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

import java.io.IOException

@RequiredArgsConstructor
@Configuration
class Html5HistoryModeResourceConfig(private val resourceProperties: ResourceProperties) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**") // 全パスをこのリソースハンドラーの処理対象にする
                .addResourceLocations(*resourceProperties.staticLocations) // 静的リソース配置先のパスを指定する
                .resourceChain(resourceProperties.chain.isCache) // 開発時はfalse、本番はtrueが望ましい。trueにしておくとメモリ上にキャッシュされるためI/Oが軽減される
                .addResolver(SpaPageResourceResolver()) // 拡張したPathResourceResolverを読み込ませる
    }

    class SpaPageResourceResolver : PathResourceResolver() {
        @Throws(IOException::class)
        override fun getResource(resourcePath: String, location: Resource): Resource? {
            val resource = super.getResource(resourcePath, location) // まずはPathResourceResolverで静的リソースを取得する
            return resource ?: super.getResource("index.html", location) // 取得できなかった場合は、index.htmlを返す
        }
    }
}
