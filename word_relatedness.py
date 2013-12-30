import resources

def inc(d, key):
  """
  increments the value of key in dict d if key exists, otherwise sets it to 1
  """
  d[key] = d.get(key, 0) + 1

def sentence_cooccurrence(sents):
  """
  usage sentence_coocurrence( [[word]] ) -> {word : {word : rel score}}
  
  builds and returns a map from individual words that occur in the argument, a list of lists of words,
  to {word : score} maps based on the co-occurrence of words within the sublists. scores are given as a ratio of all co-occurring words.
  """
  word_rel_map = {}
  total = 0

  # get initial co-occurrence counts
  for sentence in sents:
    for r_word in sentence:
      word = resources.standardize(r_word)
      if word not in resources.non_content_words:
        word_rel_map[word] = word_rel_map.get(word, {})
        for r_other in sentence:
	  other = resources.standardize(r_other)
	  if other not in resources.non_content_words:
	    inc(word_rel_map[word], other)
	  
  # normalize as ratio of all co-ocurring words
  for word in word_rel_map:
    word_cooccurrence_total = float(sum(word_rel_map[word].values()))
    for other in word_rel_map[word]:
      word_rel_map[word][other] = word_rel_map[word][other]/ word_cooccurrence_total
      
  return word_rel_map