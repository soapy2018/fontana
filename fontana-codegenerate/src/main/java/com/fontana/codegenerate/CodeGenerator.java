package com.fontana.codegenerate;


import com.fontana.codegenerate.support.MyCodeGenerator;

/**
 * 代码生成器
 *
 * @author cqf
 */
public class CodeGenerator {
	
	/**
	 * RUN THIS
	 */
	public static void run() {
		MyCodeGenerator generator = new MyCodeGenerator();
		generator.run();
	}

	public static void main(String[] args) {
		CodeGenerator.run();
	}

}
