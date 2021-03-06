package org.ares.app.common.cfg;

import static org.ares.app.common.cfg.Params.*;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.ares.app.trans.daos.UserDao;
import org.ares.app.trans.entities.SUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

@Configuration
@EnableWebSecurity
/*@EnableGlobalMethodSecurity(prePostEnabled = true)*/
public class SysWebSecCfg extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.authorizeRequests()
		.anyRequest().authenticated()
		.antMatchers("/**").hasRole(ROLE_ADMIN)
		.and().formLogin().loginProcessingUrl(LOGIN_URL).loginPage(LOGIN_PAGE_URL).defaultSuccessUrl(INDEX_URL).permitAll()
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL)).logoutSuccessUrl(LOGIN_PAGE_URL).permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(JS_URL, CSS_URL, IMG_URL, FAVICON_ICO_URL,REGISGER_URL,API_URL,H2_DB_CONSOLE,OTHER_API_URL,FONT_URL);
		//web.ignoring().antMatchers(REGISGER_URL,API_URL,H2_DB_CONSOLE);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String uname) throws UsernameNotFoundException {
				if (StringUtils.isEmpty(uname)) {
					throw new UsernameNotFoundException("username is null");
				}
				SUser user = ud.findByUsername(uname);
				if (user == null)
					throw new UsernameNotFoundException("username not found");
				Set<GrantedAuthority> auth = new HashSet<>();
				auth.add(new SimpleGrantedAuthority(user.getRole()));
				return new User(uname, user.getPassword(), !StringUtils.isEmpty(user.getRole()), true, true, true, auth);
			}

		});
	}

	@Resource
	UserDao ud;

}
