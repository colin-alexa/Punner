import PackageLoader
import WordGraph
import itertools
from Tokenizer import Corpus

wg = WordGraph();
print "createDb"
wg.createDb();
print "..."

tfile = "../test/test.txt"

text = Corpus(tfile)

print text.sents[:5]

raw_input("any key to continue")

for w,v in itertools.chain.from_iterable(text.sentsProduct()):
  print w, v
  wg.createWordAssociation(w, v)


for word in wg.wordsLike("magic"):
  print word