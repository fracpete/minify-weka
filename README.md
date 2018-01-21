# minify-weka
Tool for reducing Weka to a minimal subset of classes. 
Currently updates the `GUIEditors.props` and `GenericPropertiesCreator.props`
properties files.

Tested with: 3.6, 3.8 and 3.9

## Command-line help
When using `-h` or `--help` you get the following help screen:

```
usage: com.github.fracpete.minify.Weka
       [-h] --java-home JAVAHOME [--ant-home ANTHOME]
       --classes CLASSES [--additional ADDITIONAL] --input INPUT
       --output OUTPUT [--test] packages [packages ...]

positional arguments:
  packages               The packages to keep, eg 'weka'.

optional arguments:
  -h, --help             show this help message and exit
  --java-home JAVAHOME   The java home directory  of  the JDK that includes
                         the jdeps binary, default  is taken from JAVA_HOME
                         environment variable.
  --ant-home ANTHOME     The  ant   home   directory   (above   the   'bin'
                         directory), if not on PATH.
  --classes CLASSES      The file containing the  classes  to determine the
                         dependencies for. Empty  lines  and lines starting
                         with # get ignored.
  --additional ADDITIONAL
                         The file  with  additional  class  names  to  just
                         include.
  --input INPUT          The directory with the  pristing build environment
                         in.
  --output OUTPUT        The  directory  for  storing  the  minified  build
                         environment in.
  --test                 Optional   testing   of    the    minified   build
                         environment.
```

## Example

Classes to include (`classes_38.txt`):

```
weka.classifiers.Evaluation
weka.classifiers.functions.Logistic
weka.classifiers.functions.MultilayerPerceptron
weka.core.Attribute
weka.core.DenseInstance
weka.core.Instances
weka.core.SerializationHelper
weka.filters.Filter
weka.filters.unsupervised.attribute.Discretize
weka.filters.supervised.instance.Resample
```

Classes that cannot be located with [deps4j](https://github.com/fracpete/deps4j)
(`additional_38.txt`) and must be supplied separately (empty lines and lines 
starting with `#` get ignored):

```
# expressions
weka.core.expressionlanguage.common.Primitives

# others
weka.gui.LogPanel
weka.experiment.Stats
weka.core.matrix.Maths

# PMML
weka.core.pmml.jaxbbindings.ACTIVATIONFUNCTION
weka.core.pmml.jaxbbindings.Aggregate
weka.core.pmml.jaxbbindings.Alternate
...  
```

Command-line for generating a minified version of Weka:
```bash
java com.github.fracpete.minify.Weka
  --java-home /somepath/jdk/jdk1.8.0_144-64bit \
  --input /someplace/weka-3.8/weka/ \
  --output /elsewhere/wekaout/ \
  --classes /elsewhere/classes_38.txt \
  --additional /elsewhere/additional_38.txt \
  weka
```

## Maven
Use the following dependency in your `pom.xml`:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>minify-weka</artifactId>
      <version>0.0.6</version>
    </dependency>
```

## Releases

The `bin` directory of the zip file contains a batch file and a bash script
for making execution easier.

* [0.0.6](https://github.com/fracpete/minify-weka/releases/download/minify-weka-0.0.6/minify-weka-0.0.6-bin.zip)
* [0.0.5](https://github.com/fracpete/minify-weka/releases/download/minify-weka-0.0.5/minify-weka-0.0.5-bin.zip)
* [0.0.4](https://github.com/fracpete/minify-weka/releases/download/minify-weka-0.0.4/minify-weka-0.0.4-bin.zip)
* [0.0.3](https://github.com/fracpete/minify-weka/releases/download/minify-weka-0.0.3/minify-weka-0.0.3-bin.zip)
* [0.0.2](https://github.com/fracpete/minify-weka/releases/download/minify-weka-0.0.2/minify-weka-0.0.2-bin.zip)
