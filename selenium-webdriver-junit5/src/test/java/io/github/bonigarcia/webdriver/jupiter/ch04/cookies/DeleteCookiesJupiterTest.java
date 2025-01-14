/*
 * (C) Copyright 2021 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.webdriver.jupiter.ch04.cookies;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

class DeleteCookiesJupiterTest {

    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        // FIXME: pause for manual browser inspection
        Thread.sleep(Duration.ofSeconds(3).toMillis());

        driver.quit();
    }

    @Test
    void testDeleteCookies() {
        driver.get(
                "https://bonigarcia.dev/selenium-webdriver-java/cookies.html");

        Options options = driver.manage();

        //cookies is the set of cookie BEFORE we deleted that one cookie
        Set<Cookie> cookies = options.getCookies();

        Cookie cookie = options.getCookieNamed("username");

        //Everytime we use options we are calling the website
        options.deleteCookie(cookie);

        Cookie doesntExist = new Cookie("favorite-flavor", "chocolate chip");

        options.deleteCookie(doesntExist);

        //get the latest cookies from the website
        Set<Cookie> latestCookiesOnWeb = options.getCookies();
        boolean found = false;
        for (Cookie c: latestCookiesOnWeb) {
            if (c.getName().equals("username")) {
                found = true;
            }
        }
        assertThat(found).isFalse();//pass
        assertThat(latestCookiesOnWeb).hasSize(cookies.size() - 1);//pass
        assertThat(options.getCookieNamed("username")).isNull(); //pass
        assertThat(cookie).isNotNull();

        driver.findElement(By.id("refresh-cookies")).click();
    }

}
