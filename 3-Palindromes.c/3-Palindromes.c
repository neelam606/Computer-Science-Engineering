#include <iostream>

#include <cstring>

#include <vector>

using namespace std;

typedef long long llong;

// Manacher's Algorithm
void longest_palindromes(const char* S, int N, vector<int>& L) {
   L.clear();
   int pal_len = 0;
   for (int i = 0; i < N; ) {
      if (i > pal_len && S[ i - pal_len - 1 ] == S[i]) {
         pal_len += 2;
         ++i;
         continue;
      }
      L.push_back(pal_len);

      bool found = false;
      for (int start = int(L.size())-2, end = start-pal_len, j = start;
           j > end; --j) {
         int d = j - end - 1;
         if (L[j] == d) {
            pal_len = d;
            found = true;
            break;
         }
         L.push_back(min(d, L[j]));
      }
      if (!found) {
         pal_len = 1;
         ++i;
      }
   }
   L.push_back(pal_len);

   for (int start = int(L.size())-2, end = start-(2*N + 1 - int(L.size())),
            j = start; j > end; --j) {
      int d = j - end - 1;
      L.push_back(min(d, L[j]));
   }
}

#define MAXLEN 1000004

int N;
char S[MAXLEN];
int dig3[MAXLEN];
int prefsum[MAXLEN];
int nz_count[3][MAXLEN];

#undef DEB

int main(int argc, char* argv[]) {
   ios_base::sync_with_stdio(false); 
   cin.tie(NULL);

   int sum3 = 0;
   cin >> S+1;
   for (int i = 1; S[i]; ++i, ++N) {
      dig3[i] = (S[i]-'0') % 3;
      sum3 = (sum3 + dig3[i]) % 3;
      prefsum[i] += sum3;
      if (prefsum[i] >= 3) prefsum[i] -= 3;
      nz_count[0][i] = nz_count[0][i-1];
      nz_count[1][i] = nz_count[1][i-1];
      nz_count[2][i] = nz_count[2][i-1];
      if (S[i] != '0')
         ++nz_count[ sum3 ][i];
#ifdef DEB
      cerr << i << ": " << sum3 << "  "
           << nz_count[0][i] << ' '
           << nz_count[1][i] << ' '
           << nz_count[2][i] << endl;
#endif
   }

   vector<int> V;
   longest_palindromes(S+1, N, V);

#ifdef DEB
   cerr << "Manacher Odd" << endl;
#endif

   llong res = 0;
   // odd palindromes
   for (int i = 1; i <= N; ++i) {
      int k = i*2 - 1;
      int mx_len = V[k];
      int half_len = mx_len / 2;
      int dig = dig3[i];
      int psum = prefsum[i];
      int x = (dig + psum) % 3;
      llong add = nz_count[x][i + half_len] - nz_count[x][i-1];
#ifdef DEB
      cerr << i << " -> " << k << " : " << mx_len << ' '
           << dig << ' ' << psum  << ' ' << x << ' ' << "+" << add << endl;
#endif
      res += add;
      if (S[i] == '0')
         ++res;
   }

#ifdef DEB
   cerr << "Manacher Even" << endl;
#endif

   for (int i = 2; i <= N; ++i) {
      int k = (i-1)*2;
      int mx_len = V[k];
      int half_len = mx_len / 2;
      int dig = dig3[i];
      int x = prefsum[i-1];
      llong add = nz_count[x][i + half_len - 1] - nz_count[x][i-1];
      res += add;
#ifdef DEB
      cerr << i << " -> " << k << " : " << mx_len << ' '
           << dig << ' ' << x << ' ' << "+" << add << endl;
#endif
   }

   cout << res << endl;

   return 0;
}
