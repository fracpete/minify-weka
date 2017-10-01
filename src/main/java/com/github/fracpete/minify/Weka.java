/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Weka.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.minify;

import com.github.fracpete.deps4j.MinDeps;
import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;

/**
 * Minifies a Weka build environment using a specified minimum set of classes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Weka {

  /** the java home directory to use. */
  protected File m_JavaHome;

  /** the file with classes to determine the minimum dependencies for. */
  protected File m_ClassesFile;

  /** the file with additional class names to include (optional). */
  protected File m_AdditionalFile;

  /** the input build env. */
  protected File m_Input;

  /** the output build env. */
  protected File m_Output;

  /** whether to test the build environment. */
  protected boolean m_Test;

  /**
   * Initializes the minifier.
   */
  public Weka() {
    super();

    m_JavaHome       = null;
    m_ClassesFile    = null;
    m_AdditionalFile = null;
    m_Input          = null;
    m_Output         = null;
    m_Test           = false;
  }

  /**
   * Sets the java home directory.
   *
   * @param value	the directory
   */
  public void setJavaHome(File value) {
    m_JavaHome = value;
  }

  /**
   * Returns the java home directory.
   *
   * @return		the directory
   */
  public File getJavaHome() {
    return m_JavaHome;
  }

  /**
   * Sets the file with the class files to inspect.
   *
   * @param value	the file
   */
  public void setClassesFile(File value) {
    m_ClassesFile = value;
  }

  /**
   * Returns the file with the class files to inspect.
   *
   * @return		the file, null if not set
   */
  public File getClassesFile() {
    return m_ClassesFile;
  }

  /**
   * Sets the file with the additional classnames to include (optional).
   *
   * @param value	the file
   */
  public void setAdditionalFile(File value) {
    m_AdditionalFile = value;
  }

  /**
   * Returns the file with the additional classnames to include (optional).
   *
   * @return		the file, null if not set
   */
  public File getAdditionalFile() {
    return m_AdditionalFile;
  }

  /**
   * Sets the directory to use as input build environment.
   *
   * @param value	the directory
   */
  public void setInput(File value) {
    m_Input = value;
  }

  /**
   * Returns the directory to use as input build environment.
   *
   * @return		the directory, null if not set
   */
  public File getInput() {
    return m_Input;
  }

  /**
   * Sets the directory for the output build environment.
   *
   * @param value	the directory
   */
  public void setOutput(File value) {
    m_Output = value;
  }

  /**
   * Returns the directory for output build environment.
   *
   * @return		the directory, null if not set
   */
  public File getOutput() {
    return m_Output;
  }

  /**
   * Sets whether to test the minified build env.
   *
   * @param value	true if to test
   */
  public void setTest(boolean value) {
    m_Test = value;
  }

  /**
   * Returns whether to test the minified build env.
   *
   * @return		true if to test
   */
  public boolean getTest() {
    return m_Test;
  }

  /**
   * Sets the commandline options.
   *
   * @param options	the options to use
   * @return		true if successful
   * @throws Exception	in case of an invalid option
   */
  public boolean setOptions(String[] options) throws Exception {
    ArgumentParser parser;
    Namespace ns;

    parser = ArgumentParsers.newArgumentParser(MinDeps.class.getName());
    parser.addArgument("--java-home")
      .type(Arguments.fileType().verifyExists().verifyIsDirectory())
      .dest("javahome")
      .required(true)
      .help("The java home directory of the JDK that includes the jdeps binary, default is taken from JAVA_HOME environment variable.");
    parser.addArgument("--classes")
      .type(Arguments.fileType().verifyExists().verifyIsFile().verifyCanRead())
      .dest("classes")
      .required(true)
      .help("The file containing the classes to determine the dependencies for. Empty lines and lines starting with # get ignored.");
    parser.addArgument("--additional")
      .type(Arguments.fileType())
      .setDefault(new File("."))
      .required(false)
      .dest("additional")
      .help("The file with additional class names to just include.");
    parser.addArgument("--input")
      .type(Arguments.fileType())
      .setDefault(new File("."))
      .required(true)
      .dest("input")
      .help("The directory with the pristing build environment in.");
    parser.addArgument("--output")
      .type(Arguments.fileType().verifyIsDirectory().verifyExists())
      .setDefault(new File("."))
      .required(true)
      .dest("output")
      .help("The directory for storing the minified build environment in.");
    parser.addArgument("--test")
      .action(Arguments.storeTrue())
      .required(false)
      .dest("test")
      .help("Optional testing of the minified build environment.");

    try {
      ns = parser.parseArgs(options);
    }
    catch (ArgumentParserException e) {
      parser.handleError(e);
      return false;
    }

    setJavaHome(ns.get("javahome"));
    setClassesFile(ns.get("classes"));
    setAdditionalFile(ns.get("additional"));
    setInput(ns.get("input"));
    setOutput(ns.get("output"));
    setTest( ns.getBoolean("test"));

    return true;
  }

  /**
   * Initializes the execution.
   */
  protected void initialize() {
  }

  /**
   * Performs some checks.
   *
   * @return		null if successful, otherwise error message
   */
  protected String check() {
    if (!m_JavaHome.exists())
      return "Java home directory does not exist: " + m_JavaHome;
    if (!m_JavaHome.isDirectory())
      return "Java home does not point to a directory: " + m_JavaHome;

    if (!m_ClassesFile.exists())
      return "File with class names does not exist: " + m_ClassesFile;
    if (m_ClassesFile.isDirectory())
      return "File with class names points to directory: " + m_ClassesFile;

    if (!m_Input.exists())
      return "Input build environment does not exist: " + m_Input;
    if (!m_Input.isDirectory())
      return "Input build environment points to a file: " + m_Input;

    return null;
  }

  /**
   * Builds the specified Weka environment.
   *
   * @param dir		the build env
   * @return		null if successful, otherwise error message
   */
  protected String build(File dir) {
    String[] 			cmd;
    ProcessBuilder 		builder;
    CollectingProcessOutput 	output;

    cmd = new String[]{
      "ant",
      "clean",
      "exejar",
    };
    builder = new ProcessBuilder();
    builder.command(cmd);
    output = new CollectingProcessOutput();
    try {
      output.monitor(builder);
    }
    catch (Exception e) {
      return "Failed to execute: " + builder.toString() + "\n" + e;
    }

    return null;
  }

  /**
   * Minifies the build environment.
   *
   * @return		null if successful, otherwise error message
   */
  protected String minify() {
    MinDeps		min;

    // TODO

    return null;
  }

  /**
   * Determines the dependencies.
   *
   * @return		null if successful, otherwise error message
   */
  public String execute() {
    String		result;

    initialize();

    result = check();

    if (result == null) {
      result = build(m_Input);
      if (result != null)
        result = "Failed to build input build environment: " + result;
    }

    if (result == null)
      result = minify();

    if (result == null) {
      result = build(m_Output);
      if (result != null)
        result = "Failed to build minified build environment: " + result;
    }

    return result;
  }

  public static void main(String[] args) throws Exception {
    Weka 	weka;
    String	error;

    weka = new Weka();
    if (weka.setOptions(args)) {
      error = weka.execute();
      if (error != null) {
	System.err.println(error);
	System.exit(2);
      }
    }
    else {
      System.exit(1);
    }
  }
}
