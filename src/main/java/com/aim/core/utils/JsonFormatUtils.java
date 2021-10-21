package com.aim.core.utils;

/**
 * @AUTO Json字符串格式化工具
 * @Author AIM
 * @DATE 2021/10/21
 */
public class JsonFormatUtils {

    /**
     * 格式化json字符串
     *
     * @param jsonStr 未格式化前的JSON窜
     * @return 格式化好的JSON窜
     */
    public static String formatJson(String jsonStr) {
        if (AimUtil.isBlank(jsonStr)) {
            return AimUtil.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        char last;
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb     要追加空格的字符串
     * @param indent 追加的空格数
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

}
