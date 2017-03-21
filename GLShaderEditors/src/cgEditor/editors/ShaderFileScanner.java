/**
 * 
 */
package cgEditor.editors;

import java.util.BitSet;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;

import cgEditor.preferences.PreferenceConstants;

/**
 * @author Martinez
 * 
 */
public class ShaderFileScanner extends RuleBasedScanner {

	char escChar[] = { '\n', '\r', ' ', '\t', '.', ';', ',', '(', ')', '[', ']' };

	String language[];

	String types[];

	String functions[];

	String semantics[];

	String[] keys = null;

	/**
	 * 
	 */
	public ShaderFileScanner() {
		super();

	}
	
	protected class MyWordDetector implements IWordDetector {

		protected BitSet set = new BitSet(keys.length);

		protected int index = 0;
		
		@Override
		public boolean isWordStart(char c) {
			set.clear();
			index = 1;
			boolean start = false;
			for (int i = 0; i < keys.length; i++) {

				if (!keys[i].isEmpty()) {
					if (keys[i].charAt(0) == c) {
						set.set(i);
						start = true;
					}
				}
			}
			return start;
		}

		@Override
		public boolean isWordPart(char c) {
			if (set.isEmpty()) {
				return isNotEscChar(c);
			}
			boolean inc_index = false;
			for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i+1)) {
				boolean is_parse = true;
				if (index == 1 && keys[i].charAt(0) == '#' && (c == ' ' || c == '\t')) {
					is_parse = false;
				}
				if ( is_parse ) {
					if (keys[i].length() > index) {
						if (keys[i].charAt(index) != c) {
							set.clear(i);
						}
					} else {
						set.clear(i);
					}
					inc_index = true;
				}
			}
			if ( inc_index ) {
				index++;
			}
			return !set.isEmpty() || isNotEscChar(c);
		}

		public boolean isNotEscChar(char c) {
			for (int i = 0; i < escChar.length; i++) {
				if (c == escChar[i]) {
					return false;
				}
			}
			return true;
		}
		
		public int getInded() {
			return index;
		}
		
		public BitSet getBitSet() {
			return set;
		}

	}

	public void setShaderRules() {

		if (keys == null) {
			keys = new String[language.length + types.length + functions.length + semantics.length];
			System.arraycopy(language, 0, keys, 0, language.length);
			System.arraycopy(types, 0, keys, language.length, types.length);
			System.arraycopy(functions, 0, keys, language.length + types.length, functions.length);
			System.arraycopy(semantics, 0, keys, language.length + types.length + functions.length, semantics.length);
		}

		IToken commentToken = TokenManager.getToken(PreferenceConstants.COMMENTSTRING);
		IToken languageToken = TokenManager.getToken(PreferenceConstants.LANGUAGESSTRING);
		IToken typeToken = TokenManager.getToken(PreferenceConstants.TYPESSTRING);
		IToken functionToken = TokenManager.getToken(PreferenceConstants.FUNCTIONSSTRING);
		IToken semanticToken = TokenManager.getToken(PreferenceConstants.SEMANTICSSTRING);
		IToken defaultToken = TokenManager.getToken(PreferenceConstants.DEFAULTSTRING);

		IRule[] rules = new IRule[3];
		// Add rule for processing instructions
		rules[0] = new MultiLineRule("/*", "*/", commentToken);
		rules[1] = new EndOfLineRule("//", commentToken);
		rules[2] = new WordRuleEx(new MyWordDetector(), defaultToken);

		for (int i = 0; i < language.length; i++)
			((WordRuleEx) rules[2]).addWord(language[i], languageToken);

		for (int i = 0; i < types.length; i++)
			((WordRuleEx) rules[2]).addWord(types[i], typeToken);

		for (int i = 0; i < semantics.length; i++)
			((WordRuleEx) rules[2]).addWord(semantics[i], semanticToken);

		for (int i = 0; i < functions.length; i++)
			((WordRuleEx) rules[2]).addWord(functions[i], functionToken);

		setRules(rules);
	}
}
