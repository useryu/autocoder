package cn.agilecode.autocoder.metadata;

import java.util.HashSet;
import java.util.Set;

import cn.agilecode.autocoder.util.StrKit;

/**
 * JavaKeyword.
 */
public class JavaKeyword {
	
	private String[] keywordArray = {
		"abstract",
		"assert",
		"boolean",
		"break",
		"byte",
		"case",
		"catch",
		"char",
		"class",
		"const",
		"continue",
		"default",
		"do",
		"double",
		"else",
		"enum",
		"extends",
		"final",
		"finally",
		"float",
		"for",
		"goto",
		"if",
		"implements",
		"import",
		"instanceof",
		"int",
		"interface",
		"long",
		"native",
		"new",
		"package",
		"private",
		"protected",
		"public",
		"return",
		"strictfp",
		"short",
		"static",
		"super",
		"switch",
		"synchronized",
		"this",
		"throw",
		"throws",
		"transient",
		"try",
		"void",
		"volatile",
		"while"
	};
	
	private Set<String> set = initKeyword();
	
	public void addKeyword(String keyword) {
		if (StrKit.notBlank(keyword)) {
			set.add(keyword);
		}
	}
	
	private Set<String> initKeyword() {
		HashSet<String> ret = new HashSet<String>();
		for (String keyword : keywordArray) {
			ret.add(keyword);
		}
		return ret;
	}
	
	public boolean contains(String str) {
		return set.contains(str);
	}
}






