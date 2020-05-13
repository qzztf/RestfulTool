/*
  Copyright (C), 2018-2020, ZhangYuanSheng
  FileName: GotoRequestMappingProvider
  Author:   ZhangYuanSheng
  Date:     2020/5/12 20:49
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package core.view.search;

import com.intellij.ide.util.gotoByName.ChooseByNameBase;
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author ZhangYuanSheng
 * @version 1.0
 */
public class GotoRequestProvider extends DefaultChooseByNameItemProvider {

    public GotoRequestProvider(@Nullable PsiElement context) {
        super(context);
    }

    @NotNull
    private static MinusculeMatcher buildPatternMatcher(@NotNull String pattern, @NotNull NameUtil.MatchingCaseSensitivity caseSensitivity) {
        return NameUtil.buildMatcher(pattern, caseSensitivity);
    }

    @NotNull
    public static String removeRedundancyMarkup(@NotNull String pattern) {
        String localhostRegex = "(http(s?)://)?(localhost)(:\\d+)?";
        String hostAndPortRegex = "(http(s?)://)?" +
                "( " +
                "([a-zA-Z0-9]([a-zA-Z0-9\\\\-]{0,61}[a-zA-Z0-9])?\\\\.)+[a-zA-Z]{2,6} |" +  // domain
                "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)" + // ip address
                ")";

        String localhost = "localhost";
        if (pattern.contains(localhost)) {
            pattern = pattern.replaceFirst(localhostRegex, "");
        }

        if (pattern.contains("http:") || pattern.contains("https:")) {
            // quick test if reg exp should be used
            pattern = pattern.replaceFirst(hostAndPortRegex, "");
        }

        // 包含参数
        if (pattern.contains("?")) {
            pattern = pattern.substring(0, pattern.indexOf("?"));
        }
        return pattern;
    }

    @NotNull
    @Override
    public List<String> filterNames(@NotNull ChooseByNameBase base, @NotNull String[] names, @NotNull String pattern) {
        return super.filterNames(base, names, pattern);
    }

    @Override
    public boolean filterElements(@NotNull ChooseByNameBase base, @NotNull String pattern, boolean everywhere, @NotNull ProgressIndicator indicator, @NotNull Processor<Object> consumer) {
        pattern = removeRedundancyMarkup(pattern);
        return super.filterElements(base, pattern, everywhere, indicator, consumer);
    }
}
