package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "speakers")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Indexed
@AnalyzerDef(name = "speakerAnalyzer",
        tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class), // Replace accented characeters by their simpler counterpart (è => e, etc.)
                @TokenFilterDef(factory = LowerCaseFilterFactory.class), // Lowercase all characters
                @TokenFilterDef(
                        factory = EdgeNGramFilterFactory.class, // Generate prefix tokens
                        params = {
                                @Parameter(name = "minGramSize", value = "1"),
                                @Parameter(name = "maxGramSize", value = "10")
                        }
                )
        })
public class Speaker extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Activity activity;

    @Analyzer(definition = "speakerAnalyzer")
    @Field(termVector = TermVector.YES)
    @Column(nullable = false)
    private String firstName;

    @Analyzer(definition = "speakerAnalyzer")
    @Field(termVector = TermVector.YES)
    @Column(nullable = false)
    private String lastName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String photo;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy="speakers")
    private Set<Event> events = new HashSet<>();

}
