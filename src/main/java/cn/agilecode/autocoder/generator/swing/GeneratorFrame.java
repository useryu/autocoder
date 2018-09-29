package cn.agilecode.autocoder.generator.swing;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import javax.swing.BoxLayout;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import cn.agilecode.autocoder.dialect.Dialect;
import cn.agilecode.autocoder.dialect.MssqlDialect;
import cn.agilecode.autocoder.dialect.MysqlDialect;
import cn.agilecode.autocoder.generator.BaseGenerator;
import cn.agilecode.autocoder.generator.ControllerGenerator;
import cn.agilecode.autocoder.generator.DaoGenerator;
import cn.agilecode.autocoder.generator.DataDictionaryGenerator;
import cn.agilecode.autocoder.generator.Generator;
import cn.agilecode.autocoder.generator.ModelEditHtmlGenerator;
import cn.agilecode.autocoder.generator.ModelGenerator;
import cn.agilecode.autocoder.generator.ModelListHtmlGenerator;
import cn.agilecode.autocoder.generator.RepoGenerator;
import cn.agilecode.autocoder.generator.ServiceGenerator;
import cn.agilecode.autocoder.generator.bean.TemplateBean;
import cn.agilecode.autocoder.metadata.MetaBuilder;
import cn.agilecode.autocoder.util.FileUtils;



public class GeneratorFrame extends JFrame {

	private static final long serialVersionUID = 6636098005177941822L;
	private JPanel contentPane;
	private final String[] drivers = { "com.mysql.jdbc.Driver", "oracle.jdbc.dirver.OracleDriver" };

	/** UIManager中UI字体相关的key */
	public static String[] DEFAULT_FONT = new String[] { "Table.font", "TableHeader.font", "CheckBox.font",
			"Tree.font", "Viewport.font", "ProgressBar.font", "RadioButtonMenuItem.font", "ToolBar.font",
			"ColorChooser.font", "ToggleButton.font", "Panel.font", "TextArea.font", "Menu.font", "TableHeader.font",
			"TextField.font", "OptionPane.font", "MenuBar.font", "Button.font", "Label.font", "PasswordField.font",
			"ScrollPane.font", "MenuItem.font", "ToolTip.font", "List.font", "EditorPane.font", "Table.font",
			"TabbedPane.font", "RadioButton.font", "CheckBoxMenuItem.font", "TextPane.font", "PopupMenu.font",
			"TitledBorder.font", "ComboBox.font" };
	
	private final Object[] options = {" 确定 "," 取消 "};
	private JTextArea logArea;
	private JGeneratorPanel generatorPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// 调整默认字体
					for (int i = 0; i < DEFAULT_FONT.length; i++) {
						UIManager.put(DEFAULT_FONT[i], new Font("微软雅黑", Font.PLAIN, 13));
					}
					GeneratorFrame frame = new GeneratorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GeneratorFrame() {
		init();
	}
	
	/**
	 * 初始化布局
	 */
	void init(){

		contentPane = new JPanel();
		
		setTitle("老余代码生成器。useryu@qq.com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 936, 685);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(null);
		
		generatorPanel = new JGeneratorPanel();
		generatorPanel.setBounds(0, 5, 455, 565);
		panel.add(generatorPanel);
		
		JButton btnNewButton = new JButton("测试链接");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneratorBean generatorBean = generatorPanel.getGeneratorBean();
				Connection conn = null;
				try {
					// 装载驱动包类
					Class.forName(generatorBean.getDriverClassName());
					// 加载驱动
					conn = DriverManager.getConnection(generatorBean.getUrl(), generatorBean.getUsername(), generatorBean.getPassword());
					if(null != conn){
						showInfo("连接成功！");
					} else{
						showError("数据库连接失败，请检查后重试！");
					}
				} catch (ClassNotFoundException ex) {
					System.out.println("装载驱动包出现异常!请查正！");
					showError("装载驱动包出现异常!请查正！");
					ex.printStackTrace();
				} catch (SQLException ex) {
					System.out.println("链接数据库发生异常!");
					showError("链接数据库发生异常!");
					ex.printStackTrace();
				} finally {
					if(conn!=null) {
						try {
							conn.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		btnNewButton.setBounds(86, 596, 93, 23);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("生成代码");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateCode();
			}
		});
		btnNewButton_1.setBounds(262, 596, 93, 23);
		panel.add(btnNewButton_1);
		
		JPanel logPanel = new JPanel();
		contentPane.add(logPanel);
		logPanel.setLayout(null);
		
		logArea = new JTextArea();
		logArea.setBounds(10, 34, 440, 603);
		logPanel.add(logArea);
		
		JLabel lblNewLabel = new JLabel("日志：");
		lblNewLabel.setBounds(10, 9, 54, 15);
		logPanel.add(lblNewLabel);
		

		setResizable(false);
		setLocationRelativeTo(null);
	}

	protected void generateCode() {
		GeneratorBean generatorBean = generatorPanel.getGeneratorBean();
		Properties properties = new Properties();
		try {
			properties.put("driverClassName", generatorBean.getDriverClassName());
			properties.put("url", generatorBean.getUrl());
			properties.put("username", generatorBean.getUsername());
			properties.put("password", generatorBean.getPassword());
			String[] excludedTables = (generatorBean.getExcludedTables()).split(",");
			//以下如果按规范，则不用做任何修改
			DataSource ds = BasicDataSourceFactory.createDataSource(properties);
			DataDictionaryGenerator dictGenerator = new DataDictionaryGenerator(ds,
					generatorBean.getOutputDir());
			String coreBasePackage = generatorBean.getCoreBasePackage();
			String projectPackage = coreBasePackage;
			String modelPackageName = coreBasePackage + "." + "model";
			String daoPackageName = coreBasePackage + "." + "dao";
			String servicePackageName = coreBasePackage + "." + "service";
			String controllerPackageName = coreBasePackage + "." + "controller";
			
			String modelOutDir = generatorBean.getOutputDir() + FileUtils.genPackagePath(modelPackageName);
			String daoOutDir = generatorBean.getOutputDir() + FileUtils.genPackagePath(daoPackageName);
			String serviceOutDir = generatorBean.getOutputDir() + FileUtils.genPackagePath(servicePackageName);
			String controllerOutDir = generatorBean.getOutputDir() + FileUtils.genPackagePath(controllerPackageName);
			TemplateBean[] templates = new TemplateBean[] {
					new TemplateBean("model","model.ftl",modelOutDir,"","java"),
					new TemplateBean("dao","repo.ftl",daoOutDir,"Repository","java"),	
					new TemplateBean("serviceInf","serviceInf.ftl",serviceOutDir,"Service","java","I"),	
					new TemplateBean("serviceImpl","serviceImpl.ftl",serviceOutDir,"Service","java"),	
					new TemplateBean("controller","controller.ftl",controllerOutDir,"Controller","java"),	
			};

			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("coreBasePackage", coreBasePackage);
			vars.put("daoPackageName", daoPackageName);
			//vars.put("baseDaoPackageName", baseDaoPackageName);
			vars.put("modelPackageName", modelPackageName);
			vars.put("controllerPackage", controllerPackageName);
			vars.put("projectPackage", projectPackage);
			vars.put("servicePackageName", servicePackageName);
			vars.put("baseServicePackageName", servicePackageName);
			
			MetaBuilder metaBuilder = new MetaBuilder(ds);
			Dialect dialect = null;
			if(generatorBean.getDriverClassName().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver")) {
				dialect=new MssqlDialect(metaBuilder.getDbMeta());
			} else if(generatorBean.getDriverClassName().equals("com.mysql.jdbc.Driver")) {
				dialect=new MysqlDialect(metaBuilder.getDbMeta());
			}
			metaBuilder.setDialect(dialect);
			metaBuilder.addExcludedTable(excludedTables);
			metaBuilder.setRemovedTableNamePrefixes(generatorBean.getRemovedTableNamePrefixes());
			metaBuilder.setIncludeTableNamePrefixes(generatorBean.getIncludeTableNamePrefixe());
	
			ModelListHtmlGenerator listGenerator = new ModelListHtmlGenerator(
					generatorBean.getHtmlOutputDir(), "client_pagging_mode_list.ftl", "server_pagging_mode_list.ftl", generatorBean.isDelOldFile());
			ModelEditHtmlGenerator editGenerator = new ModelEditHtmlGenerator(
					generatorBean.getHtmlOutputDir(), "model_edit.ftl", generatorBean.isDelOldFile());
			
			Generator g = new Generator(metaBuilder, dictGenerator, listGenerator, editGenerator,
					generatorBean.isDelOldFile(), new BaseGenerator(templates, vars, generatorBean.isDelOldFile()));
			g.generate();
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			logArea.append(sf.format(date)+",生成成功，请到目录"+generatorBean.getBaseDir()+"查看生成的代码");
			logArea.append(System.lineSeparator());
			
			int response = JOptionPane.showOptionDialog(GeneratorFrame.this, "代码已经生成，是否打开输出目录？", "确认", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if(response == 0){
				//打开文件夹
				try {
					Runtime.getRuntime().exec("cmd.exe /c start "+ generatorBean.getBaseDir());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 错误提示
	 * @param title
	 * @param msg
	 */
	public void showError(String msg){
		JOptionPane.showMessageDialog(null, msg, "错误", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * 警告提示
	 * @param msg
	 */
	public void showWarning(String msg){
		JOptionPane.showMessageDialog(null, msg, "警告", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 信息提示
	 * @param title
	 * @param msg
	 */
	public void showInfo(String msg){
		JOptionPane.showMessageDialog(null, msg, "信息", JOptionPane.INFORMATION_MESSAGE);
	}
}
