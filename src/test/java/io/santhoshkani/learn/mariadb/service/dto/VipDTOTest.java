package io.santhoshkani.learn.mariadb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.santhoshkani.learn.mariadb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VipDTO.class);
        VipDTO vipDTO1 = new VipDTO();
        vipDTO1.setId(1L);
        VipDTO vipDTO2 = new VipDTO();
        assertThat(vipDTO1).isNotEqualTo(vipDTO2);
        vipDTO2.setId(vipDTO1.getId());
        assertThat(vipDTO1).isEqualTo(vipDTO2);
        vipDTO2.setId(2L);
        assertThat(vipDTO1).isNotEqualTo(vipDTO2);
        vipDTO1.setId(null);
        assertThat(vipDTO1).isNotEqualTo(vipDTO2);
    }
}
