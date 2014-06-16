### NiTE 2.2 example NetBeans project

**(Note: the data files in the last step are not included, you must copy them yourself. Why? NiTE's license.)**

1. Modify project library paths (.jars) to point to correct ones
2. Extract NiTE2 and OpenNI2 to some directory (and compile OpenNI2 if needed)
	- Copy all .so files inside both directories (there are many duplicates) to some directory
3. Adjust project properties, Run section: add VM parameter: `-Djava.library.path=<absolute path of the directory with all the .so files>`
4. Copy `<NiTE2 extraction directory>/Redist/NiTE2` directory inside project directory (`~/NetBeansProjects/NiTE2-test`) to get required data files


