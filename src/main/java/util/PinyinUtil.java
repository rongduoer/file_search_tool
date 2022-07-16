package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;

/**
 * @author: rongduo
 * @description: 拼音工具类
 * 将汉语拼音的字符映射为字母字符串
 * @date: 2022-07-16
 */
public class PinyinUtil {
    //配置拼音的格式，全局唯一
    private static final HanyuPinyinOutputFormat FORMAT;
    //项目配置的初始化操作
    static {
        FORMAT = new HanyuPinyinOutputFormat();
        //传入的字符转为全小写
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //传入的字符不带音调
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //特殊拼音由v代替 绿 -> v
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 传入任意的文件名称，将该文件名称转为字母字符串全拼和首字母小写字符串
     * @param fileName
     * @return
     */
    public static String[] getPinyinByFileName(String fileName){
        //第一个保存全拼，第二个保存首字母
        String[] ret = new String[2];

        StringBuilder allNameAppend = new StringBuilder();
        StringBuilder firstNameAppend = new StringBuilder();

        //将字符串转换为字符数组
        //遍历每一个字符,非中文的直接保留，中文的做处理
        for (char ch : fileName.toCharArray()) {
            try {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(ch,FORMAT);
                //非中文，直接保留
                if (pinyins == null || pinyins.length == 0){
                    allNameAppend.append(ch);
                    firstNameAppend.append(ch);
                } else {
                    //默认为第一个字符，不考虑多音字的情况
                    allNameAppend.append(pinyins[0]);
                    //首字母
                    firstNameAppend.append(pinyins[0].charAt(0));
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                allNameAppend.append(ch);
                firstNameAppend.append(ch);
            }
        }
        ret[0] = allNameAppend.toString();
        ret[1] = firstNameAppend.toString();
        return ret;

    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        String str = "中古哦s";
        String[] ret = getPinyinByFileName(str);
        System.out.println(Arrays.toString(ret));
    }
}
