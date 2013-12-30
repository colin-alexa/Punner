from nltk.corpus import reader, brown, abc

testReader = reader.PlaintextCorpusReader('test/', 'test.txt')

assert testReader.sents()[0] == ['What', 'Do', 'Colors', 'Have', 'to', 'do', 'With', 'Magic', ',', 'Anyway', '?']


from word_relatedness import *

testsents = brown.sents() + abc.sents()


test_result = sentence_cooccurrence(testsents)

result_file = open("test/result.py", "w") # megalith

for word in test_result:
  rfile = open("test/" + word + ".py", "w")
  rfile.write(word + " = " + repr(test_result[word]))
  rfile.close()