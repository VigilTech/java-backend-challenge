import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class URLShortenerTest {

    private static final String DEFAULT_URL = "https://short.ner/";
    public static final URLShortener URL_SHORTENER = new URLShortener();

    private static final int MAX_LENGTH = 10;
    public static final int MIN_LENGTH = 5;

    @Test
    public void generate_isUrlRandom() throws Exception {
        var url = URL_SHORTENER.generate(5);
        var secondUrl = URL_SHORTENER.generate(5);
        assertThat(url).isNotEqualTo(secondUrl);
    }

    @Test
    public void generate_urlStartsWithDefaultValue() throws Exception {
        var url = URL_SHORTENER.generate(5);

        assertThat(url).startsWith(DEFAULT_URL);
    }

    @Test
    public void generate_urlOnlyChars() throws Exception {
        var url = URL_SHORTENER.generate(5);
        var onlyRandomUrlCharacters = url.replaceAll(DEFAULT_URL, "");
        assertThat(onlyRandomUrlCharacters).matches("^[a-zA-Z]+$");
    }


    @ParameterizedTest(name = "{index} - URL Length is {0} ")
    @MethodSource("validLengthValues")
    public void generate_urlLengthIs(int inputLength) throws Exception {
        var url = URL_SHORTENER.generate(inputLength);
        var onlyRandomUrlCharacters = url.replaceAll(DEFAULT_URL, "");
        assertThat(onlyRandomUrlCharacters).hasSize(inputLength);
    }

    @ParameterizedTest(name = "{index} - URL Length {0} is lower than MAX length of " + MAX_LENGTH)
    @MethodSource("validLengthValues")
    public void generate_urlLengthLowerThanMax(int inputLength) throws Exception {
        var url = URL_SHORTENER.generate(inputLength);
        var onlyRandomUrlCharacters = url.replaceAll(DEFAULT_URL, "");
        assertThat(onlyRandomUrlCharacters).hasSize(inputLength);
    }

    @ParameterizedTest(name = "{index} - URL Length {0} is higher than MAX length of " + MAX_LENGTH)
    @ValueSource(ints = {11, 12, 15, 20, 30})
    public void generate_urlLengthHigherThanMax(int inputLength) {
        assertThatThrownBy(() -> URL_SHORTENER.generate(inputLength))
                .hasMessage("URL Length must be lower than %s".formatted(MAX_LENGTH));
    }

    @ParameterizedTest(name = "{index} - Length {0} is lower than " + MIN_LENGTH)
    @ValueSource(ints = {-1, -2, -10, -20, 4, 3, 2, 1, 0})
    public void generate_urlLengthLowerThanMinLength(int inputLength) {
        assertThatThrownBy(() -> URL_SHORTENER.generate(inputLength))
                .hasMessage("URL Length must be higher than %s".formatted(MIN_LENGTH));
    }

    public static Stream<Arguments> validLengthValues() {
        return Stream.of(
                Arguments.of(5),
                Arguments.of(6),
                Arguments.of(7),
                Arguments.of(8),
                Arguments.of(9),
                Arguments.of(10)
        );
    }

}
