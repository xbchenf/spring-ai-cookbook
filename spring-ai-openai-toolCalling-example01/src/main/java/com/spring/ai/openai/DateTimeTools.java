package com.spring.ai.openai;

import java.time.LocalDateTime;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author 寻道AI小兵
 * @since 2025年5月21日09:43:02
 * @description
 * 创建一个名为 DateTimeTools 的工具类。
 * 在 DateTimeTools 类中实现一个获取用户时区当前日期和时间的工具。
 * 该工具不接收任何参数。Spring Framework 的 LocaleContextHolder 可以提供用户的时区。
 * 该工具将定义为一个带有 @Tool 注解的方法。为了帮助模型理解何时以及为何调用此工具，我们将提供关于该工具功能的详细描述。
 *
 *
 */
@Component
public class DateTimeTools {

    @Tool(description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

}