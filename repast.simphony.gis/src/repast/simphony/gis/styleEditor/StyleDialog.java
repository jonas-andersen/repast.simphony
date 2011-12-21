/*
 * Created by JFormDesigner on Mon Jul 31 13:07:10 CDT 2006
 */

package repast.simphony.gis.styleEditor;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author User #1
 */
public class StyleDialog extends JDialog {
	public StyleDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public StyleDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private boolean completed;

	public void setMapLayer(MapLayer layer) {
		styleEditorPanel1.setMapLayer(layer);
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
		completed = true;
	}

	public boolean display() {
		setModal(true);
		setVisible(true);
		return completed;
	}

	public Style getStyle() {
		return styleEditorPanel1.getStyle();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		styleEditorPanel1 = new StyleEditorPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
								FormSpec.DEFAULT_GROW) }, RowSpec
						.decodeSpecs("fill:default:grow")));
				contentPanel.add(styleEditorPanel1, cc.xywh(1, 1, 3, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC, FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC }, RowSpec
						.decodeSpecs("pref")));

				// ---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;

	private JPanel contentPanel;

	private StyleEditorPanel styleEditorPanel1;

	private JPanel buttonBar;

	private JButton okButton;

	private JButton cancelButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}