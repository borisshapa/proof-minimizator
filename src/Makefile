JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  Main.java \
		  proof/*.java \
		  parser/*.java \
		  operations/binary/*.java \
		  operations/unary/*java \
		  minimizator/*.java \
		  expression/*.java

all: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class proof/*.class parser/*.class operations/binary/*.class operations/unary/*.class minimizator/*.class expression/*.class

run:
	java Main