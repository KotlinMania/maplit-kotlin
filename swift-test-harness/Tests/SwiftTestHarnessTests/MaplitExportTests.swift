#if canImport(Testing)
import Testing
import Maplit

@Suite("Maplit Swift Export Suite")
struct MaplitExportTests {
    @Test("Swift module loads cleanly")
    func swiftModuleLoads() {
        #expect(Bool(true), "Maplit swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import Maplit

final class MaplitExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "Maplit swift module imported cleanly")
    }
}
#endif

