package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "activities")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Indexed
@AnalyzerDef(name = "activityAnalyzer",
        tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class), // Replace accented characeters by their simpler counterpart (è => e, etc.)
                @TokenFilterDef(factory = LowerCaseFilterFactory.class), // Lowercase all characters
                @TokenFilterDef(
                        factory = EdgeNGramFilterFactory.class, // Generate prefix tokens
                        params = {
                                @org.hibernate.search.annotations.Parameter(name = "minGramSize", value = "1"),
                                @org.hibernate.search.annotations.Parameter(name = "maxGramSize", value = "10")
                        }
                )
        })
public class Activity extends AuditModel {

    @Id
    @GeneratedValue
    @Field(name = "activity_id")
    private int id;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Analyzer(definition = "activityAnalyzer")
    @Field(termVector = TermVector.YES)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String place;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String photo = "photo-ava.jpg";

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "activity", cascade = CascadeType.REMOVE)
    private Set<Shift> shifts = new HashSet<>();
}
