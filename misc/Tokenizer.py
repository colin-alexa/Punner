class Corpus:
  
  def __init__(self, filename):
    """
    A Corpus object keeps the contents of a plaintext file in a list of "sents"
     which are themselves lists of the "contentful" words in their corresponding segments of the source text
    """
    from resources import standardize, non_content_words
    
    tfile = open(filename)
    self.text = tfile.read()
    tfile.close()
    
    print "?"
    s = self.text.split(".")
    s =  [map(standardize, sentence.split()) for sentence in s]
    self.sents = [filter( lambda x: x not in non_content_words, sentence) for sentence in s]
    print "!"
    
  def sentsProduct(self):
    """
    c.sentsProduct() -> [[(x,y)(x,z)(y,z)]]
    returns a list of the cartesian products of the words in each sentence in this corpus
     sans diagonals and reversals (terms of the form x,x or y,y don't appear, and only one of x,y or y,x will appear)
    """
    result = []
    tmp = []
    for s in self.sents:
      e = enumerate(s)
      result.append( [(w, v) for i, w in e 
		             for j, v in e 
		             if i is not j and  w < v] )
    return result
    
  