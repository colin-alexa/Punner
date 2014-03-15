Punner
======

Punpy (name is WIP): a phonetically informed pun-generator
  - given a piece of text and a topic, should insert puns into the text by replacing words or word-segments with similar sounding words from the target domain.
  - Designed to be integrated into a domain-specifc text editor for homestuck fanfiction; ChumChum.


subproblems:
---

 > break words at syllable boundaries.
  - there is a well developed algorithm for breaking words for hyphenation--this is the algorithm used by LaTex and buttbot.
  - better still, I might be able to work something out using the text-to-phoneme dictionary from espeak. This would be preferable because it ought to naturally extend to novel words, and will fit better with the flow of the text (I think)

 > given a topic, generate a set of related words
  - right now I'm working on a semantic distance metric that will put words closer together based on within-sentence co-occurrence inside my training data.

 > replace chunks with phonetically similar whole words from the target domain
  - this is the part that most benfits from using espeak's phoneme data for the chunking -- I'm going to need to phonemicise the words anyways.
  - I don't think there's an objectively optimal distance metric here, so I'll have to just kind of figure it out

Recent notes:
------

The code in /misc represents (among other things) a working prototype for the word-relatedness graph.
 The code as-is is too slow to scale to any meaningful size, for multiple reasons. The start-time lag is
 common to Jython programs, as the system must compile all the relevant source to comply with both Python and Java
 language specifications, AND THEN must start the jvm before the code can even begin to execute. On top of that,
 the current structure of the algorithm to build the graph requires many read/write operations against the
 database, which are prohibitively expensive.
 
 To combat this, I plan to rewrite the code purely in Java, and change the algorithm to do most of the work
 inmain memory in large batches and then reconcile that work with the database, hopefully reducing IO costs.