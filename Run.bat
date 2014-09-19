javac -d bin -sourcepath src src/grammar/RunGrammar.java
java -cp bin grammar.RunGrammar
javac -d bin -sourcepath src src/out/RunOutput.java
java -cp bin out.RunOutput