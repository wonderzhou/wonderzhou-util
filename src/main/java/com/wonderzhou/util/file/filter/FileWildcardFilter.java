package com.wonderzhou.util.file.filter;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 文件通配符过滤,文件名不区分大小写（全路径匹配必须使用/,不使用\）
 * <pre>
 * 假设 E:/A 文件夹下有文件：1.txt,2.txt,1.zip,aaa.doc,B/a.zip,B/b.txt,B/c.log,B/a.doc,A/test.doc
 * 1.REGEX 正则表达式,注意如果填写的是子文件夹路径时,写相对路径(相对于根路径,比如下面的c))
 *   a)expression = ^.*txt 则会得到1.txt,2.txt,B/b.txt文件
 *   b)expression = ^.*txt,^.*log或者^.*txt,^.*log 则会得到1.txt,2.txt,B/b.txt,B/c.log文件
 *   c)expression = ^B/.*txt 则会得到B/b.txt文件
 *   
 * 2.WILD_CARD 通配符 *代表任意多个字符,?代表一个字符
 *   a)expression = *.txt 则会得到1.txt,2.txt,B/b.txt文件
 *   b)expression = *.txt,*.log 则会得到1.txt,2.txt,B/b.txt,B/c.log文件
 *   c)expression = B/.*txt 则会得到B/b.txt文件
 *   
 * 3.PREFIX 路径以prefix开头
 *   a)expression = A 则会得到aaa.doc,A/test.doc文件
 *   b)expression = A,B 则会得到aaa.doc,A和B子文件夹下所有文件
 *   
 * 4.SUFFIX 路径以suffix结尾
 *   a)expression = .txt 则会得到所有txt文件
 *   b)expression = .txt,.log 则会得到所有txt文件和.log文件
 * 5.INFIX 路径包含infix
 *   同上
 * </pre>
 */
public class FileWildcardFilter implements FileFilter {

    private static final long serialVersionUID = -5523279341835641321L;
    
    public static final int REGEX           = 1; //
    public static final int WILD_CARD       = 2; //*.txt,*.zip
    public static final int PREFIX          = 3; //前缀
    public static final int SUFFIX          = 4; //后缀
    public static final int INFIX           = 5; //中缀(包含)
    
    private Pattern pattern;
    private String expression;
    private boolean isExclude = false;
    //是否是全路径匹配(比如:a/*.txt，非全路径匹配仅仅匹配文件名)
    private boolean isFullPathMatch = false;
    
    public FileWildcardFilter() {
        
    }
    
    public FileWildcardFilter(int type, String expression, boolean isExclude, boolean isFullPathMatch) {
        setExpression(type, expression);
        this.isExclude = isExclude;
        this.isFullPathMatch = isFullPathMatch;
    }
    
    public FileWildcardFilter(int type, String expression, boolean isExclude) {
        this(type, expression, isExclude, false);
    }
    
    private void createRegexPattern() {
        pattern = Pattern.compile("(?i)" + expression.replaceAll(",+", "|"));
    }

    private void createWildCardPattern() {
        pattern = Pattern.compile("(?i)" + wildcardToRegex(expression, true));
    }
    
    private void createPrefixPattern() {
        pattern = Pattern.compile("(?i)^(" + wildcardToRegex(expression, false)+").*");
    }
    
    private void createSuffixPattern() {
        pattern = Pattern.compile("(?i)^.*(" + wildcardToRegex(expression, false)+")$");
    }
    
    private void createInfixPattern() {
        pattern = Pattern.compile("(?i).*(" + wildcardToRegex(expression, false)+").*");
    }
    
    private String wildcardToRegex(String wildcard, boolean isEscape) {
        StringBuilder s = new StringBuilder(wildcard.length());
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch (c) {
                case '*':
                    if(isEscape) {
                        s.append(".*"); 
                    } else {
                        s.append("\\");
                        s.append(c);
                    }
                    break;
                case '?':
                    if(isEscape) {
                        s.append("."); 
                    } else {
                        s.append("\\");
                        s.append(c);
                    }
                    break;
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        String regex = s.toString();
        regex = regex.replaceAll("\\s+", ",");
        regex = regex.replaceAll(",+", "|");
        return regex;
    }
    
    @Override
    public boolean accept(File rootFile, File file) {
        if(file.isDirectory()) {
            return true;
        }
        boolean isMatch = true;
        try {
            String relativePath = null;
            if(isFullPathMatch) {
                //相对路径
                relativePath = file.getCanonicalPath().replace(rootFile.getCanonicalPath(), "");
                if(relativePath.startsWith(File.separator)) {
                    relativePath = relativePath.substring(1).replace("\\", "/");
                }
            } else {
                relativePath = file.getName();
            }
            isMatch = matchPath(relativePath);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if(isExclude) {
            return !isMatch;
        }
        return isMatch;
    }
    
    protected synchronized boolean matchPath(String path) {
        if (pattern == null) {
            return true;
        }
        return pattern.matcher(path).matches();
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getExpression() {
        return expression;
    }

    /**
     * 设置匹配表达式
     * @param type 表达式类型，
     *      支持{@link FileWildcardFilter#WILD_CARD},{@link FileWildcardFilter#REGEX}
     *      {@link FileWildcardFilter#PREFIX},{@link FileWildcardFilter#SUFFIX},{@link FileWildcardFilter#INFIX}
     * @param expression 表达式内容,null或空字符串表示不限制
     */
    public void setExpression(int type, String expression){
        if (type != WILD_CARD && type != REGEX && type != PREFIX && type != SUFFIX && type != INFIX) {
            throw new IllegalArgumentException("不支持的表达式类型" + type);
        }
        this.expression = expression;
        if (expression == null || "".equals(expression.trim())) {
            return;
        }
        if (type == WILD_CARD) {
            createWildCardPattern();
        } else if(type == REGEX) {
            createRegexPattern();
        } else if(type == PREFIX) {
            createPrefixPattern();
        } else if(type == SUFFIX) {
            createSuffixPattern();
        } else {
            createInfixPattern();
        }
    }

    public boolean isExclude() {
        return isExclude;
    }

    public void setExclude(boolean isExclude) {
        this.isExclude = isExclude;
    }

    public boolean isFullPathMatch() {
        return isFullPathMatch;
    }

    public void setFullPathMatch(boolean isFullPathMatch) {
        this.isFullPathMatch = isFullPathMatch;
    }

}
