from nltk.corpus import reader, brown, abc, gutenberg, genesis

testReader = reader.PlaintextCorpusReader('test/', 'test.txt')

assert testReader.sents()[0] == ['What', 'Do', 'Colors', 'Have', 'to', 'do', 'With', 'Magic', ',', 'Anyway', '?']


from word_relatedness import *

testsents = brown.sents()  + abc.sents() + gutenberg.sents() + genesis.sents()

print sentence_cooccurrence(testsents)