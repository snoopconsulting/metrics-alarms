/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snoopconsulting.idi.metrics.config.sample;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.snoopconsulting.idi.metrics.config.HostPort;
import com.snoopconsulting.idi.metrics.config.ReporterConfig;
@Ignore
public class ValidateTest
{
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Ignore
    public void validateSamples() throws IOException
    {
        ReporterConfig.loadFromFileAndValidate("src/test/resources/sample/console.yaml");
        ReporterConfig.loadFromFileAndValidate("src/test/resources/sample/csv.yaml");
        ReporterConfig.loadFromFileAndValidate("src/test/resources/sample/ganglia.yaml");
        ReporterConfig.loadFromFileAndValidate("src/test/resources/sample/ganglia-gmond.yaml");
        ReporterConfig.loadFromFileAndValidate("src/test/resources/sample/ganglia-gmond-predicate.yaml");
        ReporterConfig.loadFromFileAndValidate("src/test/resources/sample/graphite.yaml");
        ReporterConfig.loadFromFileAndValidate("src/test/resources/sample/multi.yaml");
    }

    @Test
    @Ignore
    public void validationWorks() throws IOException
    {
        assertFalse(ReporterConfig.validate(new HostPort()));
    }


    @Test
    @Ignore
    public void validateMissingPeriod() throws IOException
    {
        thrown.expect(ReporterConfig.ReporterConfigurationException.class);
        ReporterConfig config = ReporterConfig.loadFromFileAndValidate("src/test/resources/invalid/missing-period.yaml");
    }

    @Test
    @Ignore
    public void validateMissingOutDir() throws IOException
    {
        thrown.expect(ReporterConfig.ReporterConfigurationException.class);
        ReporterConfig config = ReporterConfig.loadFromFileAndValidate("src/test/resources/invalid/csv-missing-outdir.yaml");
    }


    @Test
    @Ignore
    public void validateMissingPortRange() throws IOException
    {
        thrown.expect(ReporterConfig.ReporterConfigurationException.class);
        ReporterConfig config = ReporterConfig.loadFromFileAndValidate("src/test/resources/invalid/invalid-port-range.yaml");
    }

    @Test
    @Ignore
    public void validateTimeUnit() throws IOException
    {
        thrown.expect(ReporterConfig.ReporterConfigurationException.class);
        ReporterConfig config = ReporterConfig.loadFromFileAndValidate("src/test/resources/invalid/bad-timeunit.yaml");
    }


}
