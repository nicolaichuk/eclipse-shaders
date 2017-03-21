/**
 * 
 */
package cgEditor.editors;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import cgEditor.preferences.PreferenceConstants;

/**
 * @author Martinez
 *
 */
public abstract class ShaderFileConfiguration extends TextSourceViewerConfiguration {

	public ShaderFileScanner scanner;

	public ShaderFileConfiguration() {
		super();
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE,
				PreferenceConstants.COMMENTSTRING,
				PreferenceConstants.DEFAULTSTRING,
				PreferenceConstants.LANGUAGESSTRING,
				PreferenceConstants.FUNCTIONSSTRING,
				PreferenceConstants.TYPESSTRING,
				PreferenceConstants.SEMANTICSSTRING
			};
	}

	private class MyTagDamagerRepairer extends DefaultDamagerRepairer {

		public MyTagDamagerRepairer(ITokenScanner scanner) {
			super(scanner);
		}

		// TODO This method works with 3.0 and 3.1.2 but does't work well with Eclipse 3.1.1.
		@Override
		public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged) {
			if (!documentPartitioningChanged) {
				String source = fDocument.get();
				int start = source.substring(0, e.getOffset()).lastIndexOf("/*");
				if(start == -1){
					start = 0;
				}
				int end = source.indexOf("*/", e.getOffset());
				int end2 = e.getOffset() + (e.getText() == null ? e.getLength() : e.getText().length());
				if(end == -1){
					end = source.length();
				} else if(end2 > end){
					end = end2;
				} else {
					end+=2;
				}

				return new Region(start, end - start);
			}
			return partition;
		}

	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = new MyTagDamagerRepairer(getTagScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		return reconciler;
	}

	protected abstract ShaderFileScanner getTagScanner();
}
