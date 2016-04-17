package polyglot.io;

import static com.google.common.truth.Truth.assertThat;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;

import polyglot.io.testing.Testdata;
import polyglot.test.TestProto.TestRequest;

/** Unit tests for {@link FileMessageReader}. */
public class FileMessageReaderTest {
  private FileMessageReader reader;

  @Test
  public void readsSingle() {
    reader = createWithInput(Testdata.REQUEST_JSON);
    ImmutableList<DynamicMessage> result = reader.read();
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(Testdata.REQUEST);
  }

  @Test
  public void readsMultiple() {
    reader = createWithInput(Testdata.REQUEST_JSON + Testdata.REQUEST_JSON);
    ImmutableList<DynamicMessage> result = reader.read();
    assertThat(result).hasSize(2);
    assertThat(result.get(0)).isEqualTo(Testdata.REQUEST);
    assertThat(result.get(1)).isEqualTo(Testdata.REQUEST);
  }

  @Test
  public void readsEmpty() {
    reader = createWithInput("");
    ImmutableList<DynamicMessage> result = reader.read();
    assertThat(result).isEmpty();
  }

  private static FileMessageReader createWithInput(String input) {
    return new FileMessageReader(
        JsonFormat.parser(),
        TestRequest.getDescriptor(),
        new BufferedReader(new StringReader(input)));
  }
}
