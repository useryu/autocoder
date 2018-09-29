package cn.agilecode.autocoder.generator.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

import javax.swing.SwingConstants;

public class JGeneratorPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] drivers = { "com.microsoft.sqlserver.jdbc.SQLServerDriver", "com.mysql.jdbc.Driver"};
	private final String[] urlTemplate = { "jdbc:sqlserver://192.168.1.252:1433;DatabaseName=jinyuInfo", "jdbc:mysql://127.0.0.1/pm?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8"};

	private BindingGroup m_bindingGroup;
	private GeneratorBean generatorBean = new GeneratorBean();
	private JTextField baseDirJTextField;
	private JTextField coreBasePackageJTextField;
	private JCheckBox delOldFileJCheckBox;
	private JComboBox<String> driverClassNameJTextField;
	private JTextArea excludedTablesJTextArea;
	private JTextField htmlOutputDirJTextField;
	private JTextField includeTableNamePrefixeJTextField;
	private JTextField outputDirJTextField;
	private JTextField passwordJTextField;
	private JTextField projectSpecifyNameJTextField;
	private JTextField removedTableNamePrefixesJTextField;
	private JTextField urlJTextField;
	private JTextField usernameJTextField;

	public JGeneratorPanel(
			GeneratorBean newGeneratorBean) {
		this();
		setGeneratorBean(newGeneratorBean);
	}
	
	private void initDefaultSettings() {
		generatorBean.setDriverClassName("com.mysql.jdbc.Driver");
		generatorBean.setUrl("jdbc:mysql://127.0.0.1/pm?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8");
		generatorBean.setUsername("root");
		generatorBean.setPassword("123456");
		generatorBean.setExcludedTables("");
		generatorBean.setBaseDir("c:/tmp");
		String baseDir = generatorBean.getBaseDir();
		generatorBean.setOutputDir(baseDir  + "/java/");
		generatorBean.setHtmlOutputDir(baseDir + "/html/");
		generatorBean.setCoreBasePackage("com.fisher.pm");
		generatorBean.setProjectSpecifyName("pm");
		generatorBean.setIncludeTableNamePrefixe("");
		generatorBean.setRemovedTableNamePrefixes("");
		generatorBean.setDelOldFile(true);
	}

	public JGeneratorPanel() {
		initDefaultSettings();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
				0.0, 0.0 };
		setLayout(gridBagLayout);
		
				JLabel driverClassNameLabel = new JLabel("驱动:");
				driverClassNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_3 = new GridBagConstraints();
				labelGbc_3.anchor = GridBagConstraints.EAST;
				labelGbc_3.insets = new Insets(5, 5, 5, 5);
				labelGbc_3.gridx = 0;
				labelGbc_3.gridy = 0;
				add(driverClassNameLabel, labelGbc_3);
		
				driverClassNameJTextField = new JComboBox<String>(drivers);
				GridBagConstraints componentGbc_3 = new GridBagConstraints();
				componentGbc_3.insets = new Insets(5, 0, 5, 0);
				componentGbc_3.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_3.gridx = 1;
				componentGbc_3.gridy = 0;
				add(driverClassNameJTextField, componentGbc_3);
				ItemListener listener = new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED) {
							Object newVal = e.getItem();
							if(newVal.equals(drivers[0])) {
								urlJTextField.setText(urlTemplate[0]);
							} else if(newVal.equals(drivers[1])) {
								urlJTextField.setText(urlTemplate[1]);
							}
						}
					}
					
				};
				driverClassNameJTextField.addItemListener(listener );
		
				JLabel urlLabel = new JLabel("JDBC URL:");
				urlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_11 = new GridBagConstraints();
				labelGbc_11.anchor = GridBagConstraints.EAST;
				labelGbc_11.insets = new Insets(5, 5, 5, 5);
				labelGbc_11.gridx = 0;
				labelGbc_11.gridy = 1;
				add(urlLabel, labelGbc_11);
		
				urlJTextField = new JTextField();
				GridBagConstraints componentGbc_11 = new GridBagConstraints();
				componentGbc_11.insets = new Insets(5, 0, 5, 0);
				componentGbc_11.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_11.gridx = 1;
				componentGbc_11.gridy = 1;
				add(urlJTextField, componentGbc_11);
		
				JLabel usernameLabel = new JLabel("用户名:");
				usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_12 = new GridBagConstraints();
				labelGbc_12.anchor = GridBagConstraints.EAST;
				labelGbc_12.insets = new Insets(5, 5, 5, 5);
				labelGbc_12.gridx = 0;
				labelGbc_12.gridy = 2;
				add(usernameLabel, labelGbc_12);
		
				usernameJTextField = new JTextField();
				GridBagConstraints componentGbc_12 = new GridBagConstraints();
				componentGbc_12.insets = new Insets(5, 0, 5, 0);
				componentGbc_12.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_12.gridx = 1;
				componentGbc_12.gridy = 2;
				add(usernameJTextField, componentGbc_12);
		
				JLabel passwordLabel = new JLabel("密码:");
				passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_8 = new GridBagConstraints();
				labelGbc_8.anchor = GridBagConstraints.EAST;
				labelGbc_8.insets = new Insets(5, 5, 5, 5);
				labelGbc_8.gridx = 0;
				labelGbc_8.gridy = 3;
				add(passwordLabel, labelGbc_8);
		
				passwordJTextField = new JTextField();
				GridBagConstraints componentGbc_8 = new GridBagConstraints();
				componentGbc_8.insets = new Insets(5, 0, 5, 0);
				componentGbc_8.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_8.gridx = 1;
				componentGbc_8.gridy = 3;
				add(passwordJTextField, componentGbc_8);

		JLabel coreBasePackageLabel = new JLabel("包名:");
		coreBasePackageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints labelGbc_1 = new GridBagConstraints();
		labelGbc_1.anchor = GridBagConstraints.EAST;
		labelGbc_1.insets = new Insets(5, 5, 5, 5);
		labelGbc_1.gridx = 0;
		labelGbc_1.gridy = 4;
		add(coreBasePackageLabel, labelGbc_1);

		coreBasePackageJTextField = new JTextField();
		GridBagConstraints componentGbc_1 = new GridBagConstraints();
		componentGbc_1.insets = new Insets(5, 0, 5, 0);
		componentGbc_1.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_1.gridx = 1;
		componentGbc_1.gridy = 4;
		add(coreBasePackageJTextField, componentGbc_1);
				
						JLabel projectSpecifyNameLabel = new JLabel("工程名:");
						projectSpecifyNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						GridBagConstraints labelGbc_9 = new GridBagConstraints();
						labelGbc_9.anchor = GridBagConstraints.EAST;
						labelGbc_9.insets = new Insets(5, 5, 5, 5);
						labelGbc_9.gridx = 0;
						labelGbc_9.gridy = 5;
						add(projectSpecifyNameLabel, labelGbc_9);
				
						projectSpecifyNameJTextField = new JTextField();
						GridBagConstraints componentGbc_9 = new GridBagConstraints();
						componentGbc_9.insets = new Insets(5, 0, 5, 0);
						componentGbc_9.fill = GridBagConstraints.HORIZONTAL;
						componentGbc_9.gridx = 1;
						componentGbc_9.gridy = 5;
						add(projectSpecifyNameJTextField, componentGbc_9);
		
				JLabel baseDirLabel = new JLabel("输出目录:");
				baseDirLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_0 = new GridBagConstraints();
				labelGbc_0.anchor = GridBagConstraints.EAST;
				labelGbc_0.insets = new Insets(5, 5, 5, 5);
				labelGbc_0.gridx = 0;
				labelGbc_0.gridy = 6;
				add(baseDirLabel, labelGbc_0);
		
				baseDirJTextField = new JTextField();
				GridBagConstraints componentGbc_0 = new GridBagConstraints();
				componentGbc_0.insets = new Insets(5, 0, 5, 0);
				componentGbc_0.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_0.gridx = 1;
				componentGbc_0.gridy = 6;
				add(baseDirJTextField, componentGbc_0);
		
				JLabel htmlOutputDirLabel = new JLabel("Html目录:");
				htmlOutputDirLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_5 = new GridBagConstraints();
				labelGbc_5.anchor = GridBagConstraints.EAST;
				labelGbc_5.insets = new Insets(5, 5, 5, 5);
				labelGbc_5.gridx = 0;
				labelGbc_5.gridy = 7;
				add(htmlOutputDirLabel, labelGbc_5);
		
				htmlOutputDirJTextField = new JTextField();
				GridBagConstraints componentGbc_5 = new GridBagConstraints();
				componentGbc_5.insets = new Insets(5, 0, 5, 0);
				componentGbc_5.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_5.gridx = 1;
				componentGbc_5.gridy = 7;
				add(htmlOutputDirJTextField, componentGbc_5);
		
				JLabel outputDirLabel = new JLabel("java目录:");
				outputDirLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_7 = new GridBagConstraints();
				labelGbc_7.anchor = GridBagConstraints.EAST;
				labelGbc_7.insets = new Insets(5, 5, 5, 5);
				labelGbc_7.gridx = 0;
				labelGbc_7.gridy = 8;
				add(outputDirLabel, labelGbc_7);
		
				outputDirJTextField = new JTextField();
				GridBagConstraints componentGbc_7 = new GridBagConstraints();
				componentGbc_7.insets = new Insets(5, 0, 5, 0);
				componentGbc_7.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_7.gridx = 1;
				componentGbc_7.gridy = 8;
				add(outputDirJTextField, componentGbc_7);

		JLabel excludedTablesLabel = new JLabel("排除表清单:");
		excludedTablesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints labelGbc_4 = new GridBagConstraints();
		labelGbc_4.anchor = GridBagConstraints.EAST;
		labelGbc_4.insets = new Insets(5, 5, 5, 5);
		labelGbc_4.gridx = 0;
		labelGbc_4.gridy = 9;
		add(excludedTablesLabel, labelGbc_4);

		excludedTablesJTextArea = new JTextArea();
		excludedTablesJTextArea.setTabSize(18);
		excludedTablesJTextArea.setColumns(1);
		excludedTablesJTextArea.setRows(10);
		GridBagConstraints componentGbc_4 = new GridBagConstraints();
		componentGbc_4.insets = new Insets(5, 0, 5, 0);
		componentGbc_4.fill = GridBagConstraints.BOTH;
		componentGbc_4.gridx = 1;
		componentGbc_4.gridy = 9;
		add(excludedTablesJTextArea, componentGbc_4);
		
				JLabel includeTableNamePrefixeLabel = new JLabel(
						"包含前缀:");
				includeTableNamePrefixeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_6 = new GridBagConstraints();
				labelGbc_6.anchor = GridBagConstraints.EAST;
				labelGbc_6.insets = new Insets(5, 5, 5, 5);
				labelGbc_6.gridx = 0;
				labelGbc_6.gridy = 10;
				add(includeTableNamePrefixeLabel, labelGbc_6);
		
				includeTableNamePrefixeJTextField = new JTextField();
				GridBagConstraints componentGbc_6 = new GridBagConstraints();
				componentGbc_6.insets = new Insets(5, 0, 5, 0);
				componentGbc_6.fill = GridBagConstraints.HORIZONTAL;
				componentGbc_6.gridx = 1;
				componentGbc_6.gridy = 10;
				add(includeTableNamePrefixeJTextField, componentGbc_6);
		
				JLabel removedTableNamePrefixesLabel = new JLabel(
						"移除前缀:");
				removedTableNamePrefixesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints labelGbc_10 = new GridBagConstraints();
				labelGbc_10.anchor = GridBagConstraints.EAST;
				labelGbc_10.insets = new Insets(5, 5, 5, 5);
				labelGbc_10.gridx = 0;
				labelGbc_10.gridy = 11;
				add(removedTableNamePrefixesLabel, labelGbc_10);
				
						removedTableNamePrefixesJTextField = new JTextField();
						GridBagConstraints componentGbc_10 = new GridBagConstraints();
						componentGbc_10.insets = new Insets(5, 0, 5, 0);
						componentGbc_10.fill = GridBagConstraints.HORIZONTAL;
						componentGbc_10.gridx = 1;
						componentGbc_10.gridy = 11;
						add(removedTableNamePrefixesJTextField, componentGbc_10);
						
								JLabel delOldFileLabel = new JLabel("是否覆盖:");
								delOldFileLabel.setHorizontalAlignment(SwingConstants.RIGHT);
								GridBagConstraints labelGbc_2 = new GridBagConstraints();
								labelGbc_2.anchor = GridBagConstraints.EAST;
								labelGbc_2.insets = new Insets(5, 5, 5, 5);
								labelGbc_2.gridx = 0;
								labelGbc_2.gridy = 12;
								add(delOldFileLabel, labelGbc_2);
						
								delOldFileJCheckBox = new JCheckBox();
								GridBagConstraints componentGbc_2 = new GridBagConstraints();
								componentGbc_2.insets = new Insets(5, 0, 5, 0);
								componentGbc_2.fill = GridBagConstraints.HORIZONTAL;
								componentGbc_2.gridx = 1;
								componentGbc_2.gridy = 12;
								add(delOldFileJCheckBox, componentGbc_2);

		if (generatorBean != null) {
			m_bindingGroup = initDataBindings();
		}
	}

	protected BindingGroup initDataBindings() {
		BeanProperty<GeneratorBean, java.lang.String> baseDirProperty = BeanProperty
				.create("baseDir");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, baseDirProperty, baseDirJTextField,
						textProperty);
		autoBinding.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> coreBasePackageProperty = BeanProperty
				.create("coreBasePackage");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_1 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_1 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, coreBasePackageProperty,
						coreBasePackageJTextField, textProperty_1);
		autoBinding_1.bind();
		//
		BeanProperty<GeneratorBean, java.lang.Boolean> delOldFileProperty = BeanProperty
				.create("delOldFile");
		BeanProperty<javax.swing.JCheckBox, java.lang.Boolean> selectedProperty = BeanProperty
				.create("selected");
		AutoBinding<GeneratorBean, java.lang.Boolean, javax.swing.JCheckBox, java.lang.Boolean> autoBinding_2 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, delOldFileProperty, delOldFileJCheckBox,
						selectedProperty);
		autoBinding_2.bind();
		//
//		BeanProperty<GeneratorBean, java.lang.String> driverClassNameProperty = BeanProperty
//				.create("driverClassName");
//		BeanProperty<javax.swing.JComboBox<String>, java.lang.String> textProperty_2 = BeanProperty
//				.create("text");
//		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JComboBox<String>, java.lang.String> autoBinding_3 = Bindings
//				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
//						generatorBean, driverClassNameProperty,
//						driverClassNameJTextField, textProperty_2);
//		autoBinding_3.bind();

		BeanProperty<cn.agilecode.autocoder.generator.swing.GeneratorBean, java.lang.String> driverClassNameProperty = BeanProperty
				.create("driverClassName");
		BeanProperty<javax.swing.JComboBox<String>, java.lang.String> selectedIndexProperty = BeanProperty
				.create("selectedItem");
		AutoBinding<cn.agilecode.autocoder.generator.swing.GeneratorBean, java.lang.String, javax.swing.JComboBox<String>, java.lang.String> autoBinding_3 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, driverClassNameProperty,
						driverClassNameJTextField, selectedIndexProperty);
		autoBinding_3.bind();
		
		//
		BeanProperty<GeneratorBean, java.lang.String> excludedTablesProperty = BeanProperty
				.create("excludedTables");
		BeanProperty<javax.swing.JTextArea, java.lang.String> textProperty_3 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextArea, java.lang.String> autoBinding_4 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, excludedTablesProperty,
						excludedTablesJTextArea, textProperty_3);
		autoBinding_4.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> htmlOutputDirProperty = BeanProperty
				.create("htmlOutputDir");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_4 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_5 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, htmlOutputDirProperty,
						htmlOutputDirJTextField, textProperty_4);
		autoBinding_5.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> includeTableNamePrefixeProperty = BeanProperty
				.create("includeTableNamePrefixe");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_5 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_6 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, includeTableNamePrefixeProperty,
						includeTableNamePrefixeJTextField, textProperty_5);
		autoBinding_6.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> outputDirProperty = BeanProperty
				.create("outputDir");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_6 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_7 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, outputDirProperty, outputDirJTextField,
						textProperty_6);
		autoBinding_7.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> passwordProperty = BeanProperty
				.create("password");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_7 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_8 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, passwordProperty, passwordJTextField,
						textProperty_7);
		autoBinding_8.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> projectSpecifyNameProperty = BeanProperty
				.create("projectSpecifyName");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_8 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_9 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, projectSpecifyNameProperty,
						projectSpecifyNameJTextField, textProperty_8);
		autoBinding_9.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> removedTableNamePrefixesProperty = BeanProperty
				.create("removedTableNamePrefixes");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_9 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_10 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, removedTableNamePrefixesProperty,
						removedTableNamePrefixesJTextField, textProperty_9);
		autoBinding_10.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> urlProperty = BeanProperty
				.create("url");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_10 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_11 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, urlProperty, urlJTextField,
						textProperty_10);
		autoBinding_11.bind();
		//
		BeanProperty<GeneratorBean, java.lang.String> usernameProperty = BeanProperty
				.create("username");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_11 = BeanProperty
				.create("text");
		AutoBinding<GeneratorBean, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_12 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
						generatorBean, usernameProperty, usernameJTextField,
						textProperty_11);
		autoBinding_12.bind();
		//
		BindingGroup bindingGroup = new BindingGroup();
		bindingGroup.addBinding(autoBinding);
		bindingGroup.addBinding(autoBinding_1);
		bindingGroup.addBinding(autoBinding_2);
		bindingGroup.addBinding(autoBinding_3);
		bindingGroup.addBinding(autoBinding_4);
		bindingGroup.addBinding(autoBinding_5);
		bindingGroup.addBinding(autoBinding_6);
		bindingGroup.addBinding(autoBinding_7);
		bindingGroup.addBinding(autoBinding_8);
		bindingGroup.addBinding(autoBinding_9);
		bindingGroup.addBinding(autoBinding_10);
		bindingGroup.addBinding(autoBinding_11);
		bindingGroup.addBinding(autoBinding_12);
		//
		return bindingGroup;
	}

	public GeneratorBean getGeneratorBean() {
		return generatorBean;
	}

	public void setGeneratorBean(
			GeneratorBean newGeneratorBean) {
		setGeneratorBean(newGeneratorBean, true);
	}

	public void setGeneratorBean(
			GeneratorBean newGeneratorBean,
			boolean update) {
		generatorBean = newGeneratorBean;
		if (update) {
			if (m_bindingGroup != null) {
				m_bindingGroup.unbind();
				m_bindingGroup = null;
			}
			if (generatorBean != null) {
				m_bindingGroup = initDataBindings();
			}
		}
	}

}
