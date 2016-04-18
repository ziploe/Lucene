package org.ziploe.lemma;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 功能： 提取流中的  String 调用LexiconMap的lemma()进行还原 把还原之后的String复制到流中
 * @author 蒲路/Ziploe
 *
 */
public class NuposLemmaFilter extends TokenFilter{
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	public NuposLemmaFilter(TokenStream in) {
		super(in);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		if (!input.incrementToken())
			return false;

		String rawWord = new String(termAtt.buffer(), 0, termAtt.length());
		/**********changed************/
		//String rawWord = termAtt.toString();
		
		/***********************/

		String lemmaedWord = LexiconMap.lemma(rawWord);

		termAtt.copyBuffer(lemmaedWord.toCharArray(), 0, lemmaedWord.length());
		return true;
	}




}