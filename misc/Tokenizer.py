class Corpus:
  
  def __init__(self, filename):
    from resources import non_content_words
    content_pred = lambda x: x not in non_content_words
    
    tfile = open(filename)
    self.text = tfile.read()
    tfile.close()
    
    self.sents = self.text.split(".")
    self.sents = [filter(content_pred, s.split()) for s in self.sents]
    
    
  