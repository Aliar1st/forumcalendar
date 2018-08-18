package ru.forumcalendar.forumcalendar.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Indexed
@Table(name = "teams")
@EqualsAndHashCode(callSuper = false)
@AnalyzerDef(name = "customAnalyzer",
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
public class Team extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

//    @ManyToOne
//    @JoinColumn(nullable = false)
//    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Shift shift;

    @Field
    @Column(nullable = false)
    private int number;

    @Analyzer(definition = "customAnalyzer")
    @Field(termVector = TermVector.YES)
    @Column(nullable = false)
    private String name;

    @Analyzer(definition = "customAnalyzer")
    @Field(termVector = TermVector.YES)
    @Column(nullable = false)
    private String direction;

    @Analyzer(definition = "customAnalyzer")
    @Field(termVector = TermVector.YES)
    @Column(columnDefinition = "TEXT")
    private String description;

    //@IndexedEmbedded
    @OneToMany(mappedBy = "team")
    @EqualsAndHashCode.Exclude
    private Set<TeamEvent> teamEvents = new HashSet<>();

    //@IndexedEmbedded
    @OneToMany(mappedBy = "userTeamIdentity.team")
    @EqualsAndHashCode.Exclude
    private Set<UserTeam> userTeams = new HashSet<>();
}
