package thanks_card

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.AnyRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SpringSecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        // loginとlogoutを設定する。基本的な設定なので詳細は割愛
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()

        http.exceptionHandling()
                // '/api/**'へ未認証状態でのアクセスには401を返す
                .defaultAuthenticationEntryPointFor(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), AntPathRequestMatcher("/api/**"))
                // 上記パス以外への未認証状態へのアクセスは302リダイレクトで'/login'へ遷移させる
                .defaultAuthenticationEntryPointFor(LoginUrlAuthenticationEntryPoint("/login"), AnyRequestMatcher.INSTANCE)

        val cookieCsrfTokenRepository = CookieCsrfTokenRepository() // ajaxでcsrf tokenを利用するのでcookieに出力する
        cookieCsrfTokenRepository.setCookieHttpOnly(false) // ajaxでも利用するため、httpOnlyはfalseにしておく
        http.csrf().csrfTokenRepository(cookieCsrfTokenRepository)

        // RESTful APIを公開する場合、攻撃されやすくなるのでcorsの設定をしておく
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL)
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL)
        corsConfiguration.addAllowedOrigin("http://localhost:3000") // 実際は環境ごとにドメインが変わるはずなので、設定で動的に変更でき料にする
        val corsSource = UrlBasedCorsConfigurationSource()
        corsSource.registerCorsConfiguration("/**", corsConfiguration) // すべてのパスを対象にする
        http.cors().configurationSource(corsSource)
    }

    // デモ用設定
    @Bean
    public override fun userDetailsService(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()

        return InMemoryUserDetailsManager(user)
    }
}
