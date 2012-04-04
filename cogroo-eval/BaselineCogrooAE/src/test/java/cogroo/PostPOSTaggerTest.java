package cogroo;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.Span;

import org.junit.Test;
import static org.junit.Assert.*;

import br.usp.pcs.lta.cogroo.entity.Sentence;
import br.usp.pcs.lta.cogroo.entity.Token;
import br.usp.pcs.lta.cogroo.entity.impl.runtime.MorphologicalTag;
import br.usp.pcs.lta.cogroo.entity.impl.runtime.TokenCogroo;

import cogroo.uima.ae.FlorestaTagInterpreter;


public class PostPOSTaggerTest {
  

  
  @Test
  public void testMerge() {
    
    assertEquals("n=M=P", merge("n=M=P", "n=F=P"));
    assertEquals("n=M=P", merge("n=M=P", "adj=M=P"));
    assertEquals("n=M=P", merge("adj=M/F=P", "n=M=P"));
    assertEquals("n=F=P", merge("n=F=P", "n=F=P"));
    assertEquals("n=F=P", merge("v-fin=PR=3S=IND", "n=F=P"));
    assertEquals("adj=M/F=P", merge("adv", "adj=M/F=P"));
    assertEquals("adv", merge("adv", "adv"));
    assertEquals("n=F=P", merge("n=F=P", "prp"));
    assertEquals("n=F=P", merge("n=F=P", "n=F=S"));
    assertEquals("n=M=P", merge("n=M=P", "n=M=S"));
    assertEquals("n=F=P", merge("n=F=P", "n=F=S"));
    assertEquals("adv", merge("v-fin=PR=3S=IND", "adv"));
    assertEquals("n=F=P", merge("v-fin=PR=3S=IND", "n=F=P"));
    assertEquals("n=M=S", merge("det=F=S", "n=M=S"));
  }

  
  FlorestaTagInterpreter ti = new FlorestaTagInterpreter();
  private String merge(String tagA, String tagB) {
    PostPOSTagger ppt = new PostPOSTagger();
    
    Token a = new TokenCogroo(new Span(0, 5));
    Token b = new TokenCogroo(new Span(6, 10));
    a.setLexeme("strA");
    b.setLexeme("strB");
    a.setPrimitive("strA");
    b.setPrimitive("strB");
    
    Token h = new TokenCogroo(new Span(5, 6));
    h.setLexeme("-");
    
    a.setMorphologicalTag(ti.parseMorphologicalTag(tagA));
    b.setMorphologicalTag(ti.parseMorphologicalTag(tagB));
    
    List<Token> tokens = new ArrayList<Token>();
    tokens.add(a);
    tokens.add(h);
    tokens.add(b);
    
    Sentence s = new Sentence();
    s.setTokens(tokens);
    
    ppt.process(s);
    
    
    return ti.serialize(s.getTokens().get(0).getMorphologicalTag());
  }

}
