package com.darkotech.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.darkotech.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuhorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Auhority.class);
        Auhority auhority1 = new Auhority();
        auhority1.setId(1L);
        Auhority auhority2 = new Auhority();
        auhority2.setId(auhority1.getId());
        assertThat(auhority1).isEqualTo(auhority2);
        auhority2.setId(2L);
        assertThat(auhority1).isNotEqualTo(auhority2);
        auhority1.setId(null);
        assertThat(auhority1).isNotEqualTo(auhority2);
    }
}
