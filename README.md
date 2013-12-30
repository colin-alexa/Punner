Punner
======

Punpy (name is WIP): a phonetically informed pun-generator
  - given a piece of text and a topic, should insert puns into the text by replacing words or word-segments with similar sounding words from the target domain.
  - Designed to be integrated into a domain-specifc text editor for homestuck fanfiction; ChumChum.


subproblems:
---

 > break words at syllable boundaries.
  - there is a well developed algorithm for breaking words for hyphenation--this is the algorithm used by LaTex and buttbot.
  - better still, I might be able to work something out using the text-to-phoneme dictionary from espeak. This would be preferable because it aught to naturally extend to novel words, and will fit better with the flow of the text (I think)

 > given a topic, generate a set of related words
  - right now I'm working on a semantic distance metric that will put words closer together based on within-sentence co-occurrence inside my training data.

 > replace chunks with phonetically similar whole words from the target domain
  - this is the part that most benfits from using espeak's phoneme data for the chunking -- I'm going to need to phonemicise the words anyways.
  - I don't think there's an objectively optimal distance metric here, so I'll have to just kind of figure it out

Recent notes:
------

The code in word_relatedness.py is part of the 'given a topic, generate a set of related words' problem
