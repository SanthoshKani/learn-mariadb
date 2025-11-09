package io.santhoshkani.learn.mariadb.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.santhoshkani.learn.mariadb.domain.Language} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LanguageDTO implements Serializable {

    private Long id;

    @NotNull
    private String language;

    private Set<CountryDTO> countries = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(Set<CountryDTO> countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LanguageDTO)) {
            return false;
        }

        LanguageDTO languageDTO = (LanguageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, languageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageDTO{" +
            "id=" + getId() +
            ", language='" + getLanguage() + "'" +
            ", countries=" + getCountries() +
            "}";
    }
}
