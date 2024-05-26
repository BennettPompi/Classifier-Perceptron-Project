JC = javac
JFLAGS =
SRCS = ${wildcard *.java}
OBJS = ${SRCS:.java=.class}
LIBS =

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

all:$(OBJS)

# modify the zip command so it's appropriate for your project
submit:
	zip submit.zip $(SRCS) Makefile HONOR
clean:
	rm -f *.class

