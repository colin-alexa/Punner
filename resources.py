from glob import glob

from nltk.corpus import stopwords

import unicodedata

resources_root = "resources/"



def remove_bookend(text, delimiter, last=True, after=True):
  """
  remove_bookend( text, delimiter, last=true, reverse=true )
  returns the portion of text after (default) or before the last (default) or first occurrence of delimiter in text
  """
  
  index = text.rindex(delimiter) if last else text.index(delimiter)
  return text[(index+len(delimiter)+1):] if after else text[:index]

non_content_words = set(stopwords.words('english'))

for rfile in glob(resources_root + "English_Function_Words_Set/*"):
  f = open(rfile)
  non_content_words |= set(remove_bookend(f.read(), "///" ).splitlines())
  
  

def punctuation_predicate(x):
  bad_categories = ('S','P')
  return unicodedata.category(x)[0] not in bad_categories

def word_predicate(x):
  return x not in non_content_words


def standardize(word):
  """
  performs a set of operations on a given string to standardize it before processing.
  """
  
  retval = word.lower()
  retval = retval if type(retval) is 'unicode' else unicode(retval, errors='ignore')
  retval = filter( punctuation_predicate, retval )
  return str(retval)