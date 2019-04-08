package pl.niewiemmichal.underhiseye.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Examination {

    @Id
    @Column(length = 6)
    private String code;

    @NonNull
    @Column(nullable = false)
    private String name;

}
