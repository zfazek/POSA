#include <iostream>
#include <thread>
#include <mutex>

using namespace std;

mutex m;
bool is_ping_turn;
int N = 3;

void printPing() {
    for (int i = 0; i < N; i++) {
        while(! is_ping_turn) {}
        m.lock();
        is_ping_turn = false;
        cout << "Ping!" << endl;
        m.unlock();
    }
}

void printPong() {
    for (int i = 0; i < N; i++) {
        while(is_ping_turn) {}
        m.lock();
        is_ping_turn = true;
        cout << "Pong!" << endl;
        m.unlock();
    }
}

int main() {
    is_ping_turn = true;
    cout << "Ready... Set... Go!" << endl << endl;
    thread ping(printPing);
    thread pong(printPong);
    ping.join();
    pong.join();
    cout << "Done!" << endl;
    return 0;
}
